package org.kepocnhh.flights.util

internal interface ListTransformer<T : Any> : Transformer<T> {
    val list: Transformer<List<T>>
}
