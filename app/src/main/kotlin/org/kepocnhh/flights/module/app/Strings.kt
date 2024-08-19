package org.kepocnhh.flights.module.app

internal sealed interface Strings {
    val version: String
    val noFlights: String
    val flightFrom: String
    val flightCreated: String

    data object Ru : Strings {
        override val version = "Версия"
        override val noFlights = "Нет вылетов"
        override val flightFrom = "Вылет от %s"
        override val flightCreated = "Время создания %s"
    }
}
