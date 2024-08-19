package org.kepocnhh.flights.module.flights

import org.kepocnhh.flights.module.app.Injection
import sp.kx.logics.Logics


internal class FlightsLogics(
    private val injection: Injection,
) : Logics(injection.contexts.main) {
    private val logger = injection.loggers.create("[Flights]")
}
