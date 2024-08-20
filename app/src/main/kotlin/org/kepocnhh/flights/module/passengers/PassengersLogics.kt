package org.kepocnhh.flights.module.passengers

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import org.kepocnhh.flights.entity.Passenger
import org.kepocnhh.flights.module.app.Injection
import org.kepocnhh.flights.module.flights.Flights
import sp.kx.logics.Logics
import java.util.UUID

internal class PassengersLogics(
    private val injection: Injection,
) : Logics(injection.contexts.main) {
    private val logger = injection.loggers.create("[Passengers]")

    private val _passengers = MutableStateFlow<List<Passenger>?>(null)
    val passengers = _passengers.asStateFlow()

    fun requestPassengers(flightId: UUID) = launch {
        logger.debug("request passengers...")
        _passengers.value = withContext(injection.contexts.default) {
            injection.locals.passengers
                .filter { it.flightId == flightId }
                .sortedBy { it.created }
        }
    }

    fun deletePassenger(passenger: Passenger) = launch {
        logger.debug("delete passenger: ${passenger.id}")
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
    }
}
