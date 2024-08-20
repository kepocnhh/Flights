package org.kepocnhh.flights.module.app

internal sealed interface Strings {
    val version: String
    val noFlights: String
    val flightFrom: String
    val flightCreated: String
    val noPassengers: String
    val newPassenger: String
    val passengers: String
    val create: String
    val lastName: String
    val firstName: String
    val middleName: String
    val dateOfBirth: String
    val notSpecified: String
    val born: String
    val yes: String
    val deleteFlight: String
    val deletePassenger: String

    data object Ru : Strings {
        override val version = "Версия"
        override val noFlights = "Нет вылетов"
        override val flightFrom = "Вылет от %s"
        override val flightCreated = "Время создания %s"
        override val noPassengers = "Нет пассажиров"
        override val newPassenger = "Новый пассажир"
        override val passengers = "Пассажиры"
        override val create = "Создать"
        override val lastName = "Фамилия"
        override val firstName = "Имя"
        override val middleName = "Отчество"
        override val dateOfBirth = "Дата рождения"
        override val notSpecified = "Не указано"
        override val born = "Дата рождения %s"
        override val yes = "Да"
        override val deleteFlight = "Удалить вылет?"
        override val deletePassenger = "Удалить пассажира?"
    }
}
