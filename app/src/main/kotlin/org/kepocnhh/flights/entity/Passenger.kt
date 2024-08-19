package org.kepocnhh.flights.entity

import java.util.UUID

internal data class Passenger(
    val id: UUID,
    val person: Person,
)
