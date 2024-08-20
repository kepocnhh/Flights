package org.kepocnhh.flights.module.passengers

import android.os.Environment
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.kepocnhh.flights.BuildConfig
import org.kepocnhh.flights.entity.Passenger
import org.kepocnhh.flights.module.app.Injection
import org.kepocnhh.flights.module.flights.Flights
import sp.kx.logics.Logics
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

internal class PassengersLogics(
    private val injection: Injection,
) : Logics(injection.contexts.main) {
    sealed interface Event {
        data class OnExport(val file: File) : Event
    }

    private val logger = injection.loggers.create("[Passengers]")

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _passengers = MutableStateFlow<List<Passenger>?>(null)
    val passengers = _passengers.asStateFlow()

    fun requestPassengers(flightId: UUID) = launch {
        logger.debug("request passengers...")
        _loading.value = true
        _passengers.value = withContext(injection.contexts.default) {
            injection.locals.passengers
                .filter { it.flightId == flightId }
                .sortedBy { it.created }
        }
        _loading.value = false
    }

    fun deletePassenger(passenger: Passenger) = launch {
        logger.debug("delete passenger: ${passenger.id}")
        _loading.value = true
        val passengers = withContext(injection.contexts.default) {
            val passengers = injection.locals.passengers.filter { it.id != passenger.id }
            injection.locals.passengers = passengers
            passengers
                .filter { it.flightId == passenger.flightId }
                .sortedBy { it.created }
        }
        if (passengers.isEmpty()) {
            Flights.events.emit(Flights.Event.OnDeleteFlight(id = passenger.flightId))
        } else {
            _passengers.value = passengers
        }
        _loading.value = false
    }

    fun exportPassengers(flightId: UUID) = launch {
        logger.debug("export passengers...")
        _loading.value = true
        val passengers = withContext(injection.contexts.default) {
            injection.locals.passengers
                .filter { it.flightId == flightId }
                .sortedBy { it.created }
        }
        val file = withContext(injection.contexts.default) {
            val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .resolve(BuildConfig.APPLICATION_ID)
            dir.mkdirs()
            val dateTimeFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.US)
            dir.resolve("flight-${dateTimeFormat.format(Date())}.xlsx")
        }
        withContext(injection.contexts.default) {
            val wb = XSSFWorkbook()
            val created = passengers.minOf { it.created }
            val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.US)
            val sheet = wb.createSheet(dateTimeFormat.format(Date(created.inWholeMilliseconds)))
            val columns = listOf(
                "№",
                "Фамилия",
                "Имя",
                "Отчество",
                "Дата рождения",
            )
            sheet.createRow(0).also { row ->
                columns.forEachIndexed { index, column ->
                    val cell = row.createCell(index)
                    cell.setCellValue(column)
                }
            }
            val dateTime = SimpleDateFormat("MM.dd.yyyy", Locale.US)
            passengers.forEachIndexed { index, passenger ->
                val rIndex = index + 1
                val row = sheet.createRow(rIndex)
                columns.forEachIndexed { cIndex, _ ->
                    val cell = row.createCell(cIndex)
                    val value = when (cIndex) {
                        0 -> "${rIndex + 1}"
                        1 -> passenger.person.lastName
                        2 -> passenger.person.firstName
                        3 -> passenger.person.middleName
                        4 -> dateTime.format(Date(passenger.born.inWholeMilliseconds))
                        else -> TODO()
                    }
                    cell.setCellValue(value)
                }
            }
            file.outputStream().use(wb::write)
        }
        _events.emit(Event.OnExport(file = file))
        _loading.value = false
    }
}
