package org.kepocnhh.flights.entity

import java.util.UUID
import kotlin.time.Duration

internal data class Passenger(
    val id: UUID,
    val person: Person,
    val born: Duration,
    val created: Duration,
    val flightId: UUID,
)
