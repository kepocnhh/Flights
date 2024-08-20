package org.kepocnhh.flights.module.passengers

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import org.kepocnhh.flights.entity.Passenger
import org.kepocnhh.flights.entity.Person
import org.kepocnhh.flights.module.app.Injection
import sp.kx.logics.Logics
import java.util.UUID
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

internal class PassengersLogics(
    private val injection: Injection,
) : Logics(injection.contexts.main) {
    private val logger = injection.loggers.create("[Passengers]")

    private val _passengers = MutableStateFlow<List<Passenger>?>(null)
    val passengers = _passengers.asStateFlow()

    fun requestPassengers(flightId: UUID) = launch {
        logger.debug("request passengers...")
        _passengers.value = withContext(injection.contexts.default) {
//            val passengers = injection.locals.passengers // todo
            val now = System.currentTimeMillis().milliseconds
            val passengers = (11..40).map { number ->
                Passenger(
                    id = UUID(0, number.toLong()),
                    created = now - 100.hours + number.hours + (number * 2).minutes,
                    flightId = flightId,
                    person = Person(
                        firstName = "firstName:$number",
                        middleName = "middleName:$number",
                        lastName = "lastName:$number",
                    ),
                    born = now - (30 * 12 * 20).days + (30 * 12 * number).days,
                )
            }
            passengers.filter { it.flightId == flightId }
        }
    }
}
