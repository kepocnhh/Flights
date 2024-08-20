package org.kepocnhh.flights.module.passengers

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import org.kepocnhh.flights.entity.Passenger
import org.kepocnhh.flights.entity.Person
import org.kepocnhh.flights.module.app.Injection
import sp.kx.logics.Logics
import java.util.UUID
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

internal class NewPassengerLogics(
    private val injection: Injection,
) : Logics(injection.contexts.main) {
    sealed interface Event {
        data class OnCreate(val passenger: Passenger) : Event
    }

    private val logger = injection.loggers.create("[NewPassenger]")

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    fun createPassenger(
        flightId: UUID,
        person: Person,
        born: Duration,
    ) = launch {
        logger.debug("create passenger...")
        _loading.value = true
        val passenger = withContext(injection.contexts.default) {
            Passenger(
                id = UUID.randomUUID(),
                created = System.currentTimeMillis().milliseconds,
                flightId = flightId,
                person = person,
                born = born,
            )
        }
        withContext(injection.contexts.default) {
            injection.locals.passengers += passenger
        }
        _events.emit(Event.OnCreate(passenger = passenger))
    }
}
