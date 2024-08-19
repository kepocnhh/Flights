package org.kepocnhh.flights.module.app

import org.kepocnhh.flights.provider.Contexts
import org.kepocnhh.flights.provider.Locals
import org.kepocnhh.flights.provider.Loggers

internal class Injection(
    val contexts: Contexts,
    val loggers: Loggers,
    val locals: Locals,
)
