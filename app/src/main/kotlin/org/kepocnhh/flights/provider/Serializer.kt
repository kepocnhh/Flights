package org.kepocnhh.flights.provider

import org.kepocnhh.flights.entity.Passenger
import org.kepocnhh.flights.util.ListTransformer

internal interface Serializer {
    val passenger: ListTransformer<Passenger>
}
