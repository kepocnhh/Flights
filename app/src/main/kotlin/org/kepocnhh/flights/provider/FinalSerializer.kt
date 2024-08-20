package org.kepocnhh.flights.provider

import org.json.JSONArray
import org.json.JSONObject
import org.kepocnhh.flights.entity.Passenger
import org.kepocnhh.flights.entity.Person
import org.kepocnhh.flights.util.ListTransformer
import org.kepocnhh.flights.util.Transformer
import java.util.UUID
import kotlin.time.Duration.Companion.milliseconds

internal object FinalSerializer : Serializer {
    override val passenger = object : ListTransformer<Passenger> {
        private fun toJSONObject(decoded: Passenger): JSONObject {
            return JSONObject()
                .put("id", decoded.id.toString())
                .put("firstName", decoded.person.firstName)
                .put("middleName", decoded.person.middleName)
                .put("lastName", decoded.person.lastName)
                .put("born", decoded.born.inWholeMilliseconds)
                .put("created", decoded.created.inWholeMilliseconds)
                .put("flightId", decoded.flightId.toString())
        }

        private fun decode(encoded: JSONObject): Passenger {
            val person = Person(
                firstName = encoded.getString("firstName"),
                middleName = encoded.getString("middleName"),
                lastName = encoded.getString("lastName"),
            )
            return Passenger(
                id = UUID.fromString(encoded.getString("id")),
                person = person,
                born = encoded.getLong("born").milliseconds,
                created = encoded.getLong("created").milliseconds,
                flightId = UUID.fromString(encoded.getString("flightId")),
            )
        }

        override val list = object : Transformer<List<Passenger>> {
            override fun encode(decoded: List<Passenger>): ByteArray {
                val array = JSONArray()
                decoded.forEach {
                    array.put(toJSONObject(decoded = it))
                }
                return array.toString().toByteArray()
            }

            override fun decode(encoded: ByteArray): List<Passenger> {
                val array = JSONArray(String(encoded))
                return (0 until array.length()).map { index ->
                    decode(encoded = array.getJSONObject(index))
                }
            }
        }

        override fun decode(encoded: ByteArray): Passenger {
            return decode(encoded = JSONObject(String(encoded)))
        }

        override fun encode(decoded: Passenger): ByteArray {
            return toJSONObject(decoded = decoded)
                .toString()
                .toByteArray()
        }
    }
}
