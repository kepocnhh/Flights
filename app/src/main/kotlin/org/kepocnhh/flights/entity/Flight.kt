package org.kepocnhh.flights.entity

import java.util.UUID
import kotlin.time.Duration

internal data class Flight(
    val id: UUID,
    val created: Duration,
)
