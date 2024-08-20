package org.kepocnhh.flights.module.flights

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import org.kepocnhh.flights.entity.Flight
import org.kepocnhh.flights.entity.Passenger
import org.kepocnhh.flights.module.app.Injection
import sp.kx.logics.Logics

internal class FlightsLogics(
    private val injection: Injection,
) : Logics(injection.contexts.main) {
    private val logger = injection.loggers.create("[Flights]")

    private val _flights = MutableStateFlow<List<Flight>?>(null)
    val flights = _flights.asStateFlow()

    private fun getFlights(passengers: List<Passenger>): List<Flight> {
        return passengers
            .groupBy { it.flightId }
            .map { (id, passengers) ->
                Flight(id = id, created = passengers.minOf { it.created })
            }.sortedBy {
                it.created
            }
    }

    fun requestFlights() = launch {
        logger.debug("request flights...")
        _flights.value = withContext(injection.contexts.default) {
            getFlights(passengers = injection.locals.passengers)
        }
    }

    fun deleteFlight(flight: Flight) = launch {
        logger.debug("delete flight: ${flight.id}")
        _flights.value = withContext(injection.contexts.default) {
            val passengers = injection.locals.passengers.filter { it.flightId != flight.id }
            injection.locals.passengers = passengers
            getFlights(passengers = passengers)
        }
    }
}
