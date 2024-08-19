package org.kepocnhh.flights.util

internal sealed interface Optional<out T : Any> {
    data object None : Optional<Nothing> {
        override fun get(): Nothing? {
            return null
        }
    }

    class Some<T : Any>(val value: T) : Optional<T> {
        override fun get(): T {
            return value
        }
    }

    fun get(): T?

    companion object {
        fun <T : Any> ofNullable(value: T?): Optional<T> {
            return when (value) {
                null -> None
                else -> Some(value = value)
            }
        }
    }
}
