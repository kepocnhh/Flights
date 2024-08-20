package org.kepocnhh.flights.module.app

internal sealed interface Strings {
    val version: String
    val noFlights: String
    val flightFrom: String
    val flightCreated: String
    val noPassengers: String
    val newPassenger: String
    val create: String
    val lastName: String
    val firstName: String
    val middleName: String

    data object Ru : Strings {
        override val version = "Версия"
        override val noFlights = "Нет вылетов"
        override val flightFrom = "Вылет от %s"
        override val flightCreated = "Время создания %s"
        override val noPassengers = "Нет пассажиров"
        override val newPassenger = "Новый пассажир"
        override val create = "Создать"
        override val lastName = "Фамилия"
        override val firstName = "Имя"
        override val middleName = "Отчество"
    }
}
