package org.kepocnhh.flights.entity

internal data class Person(
    val firstName: String,
    val middleName: String,
    val lastName: String,
) {
    companion object {
        val Empty = Person(
            firstName = "",
            middleName = "",
            lastName = "",
        )
    }
}
