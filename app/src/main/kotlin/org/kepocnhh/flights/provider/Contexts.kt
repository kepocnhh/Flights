package org.kepocnhh.flights.provider

import kotlin.coroutines.CoroutineContext

internal class Contexts(
    val main: CoroutineContext,
    val default: CoroutineContext,
)
