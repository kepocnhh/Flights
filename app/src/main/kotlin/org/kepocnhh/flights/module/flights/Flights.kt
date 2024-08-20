package org.kepocnhh.flights.module.flights

import kotlinx.coroutines.flow.MutableSharedFlow
import org.kepocnhh.flights.entity.Passenger
import java.util.UUID

internal object Flights {
    sealed interface Event {
        data class OnDeleteFlight(val id: UUID) : Event
        data class OnCreate(val passenger: Passenger) : Event
    }

    val events = MutableSharedFlow<Event>()
}
