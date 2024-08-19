package org.kepocnhh.flights.module.flights

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import org.kepocnhh.flights.entity.Flight
import org.kepocnhh.flights.module.app.Injection
import sp.kx.logics.Logics
import java.util.UUID
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

internal class FlightsLogics(
    private val injection: Injection,
) : Logics(injection.contexts.main) {
    private val logger = injection.loggers.create("[Flights]")

    private val _flights = MutableStateFlow<List<Flight>?>(null)
    val flights = _flights.asStateFlow()

    fun requestFlights() = launch {
        logger.debug("request flights...")
        _flights.value = withContext(injection.contexts.default) {
            injection.locals.passengers
                .groupBy { it.flightId }
                .map { (id, passengers) ->
                    Flight(id = id, created = passengers.minOf { it.created })
                }.sortedBy {
                    it.created
                }
            // todo
            val now = System.currentTimeMillis().milliseconds
            (11..50).map { number ->
                Flight(
                    id = UUID(0, number.toLong()),
                    created = now - 100.hours + number.hours + (number * 2).minutes,
                )
            }
        }
    }
}
