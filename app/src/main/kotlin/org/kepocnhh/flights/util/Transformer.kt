package org.kepocnhh.flights.util

internal interface Transformer<T : Any> {
    fun encode(decoded: T): ByteArray
    fun decode(encoded: ByteArray): T
}
