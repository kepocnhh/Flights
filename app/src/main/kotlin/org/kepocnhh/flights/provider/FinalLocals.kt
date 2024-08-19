package org.kepocnhh.flights.provider

import android.content.Context
import org.kepocnhh.flights.BuildConfig
import org.kepocnhh.flights.entity.Passenger
import java.util.Base64

internal class FinalLocals(
    context: Context,
    private val serializer: Serializer,
) : Locals {
    private val prefs = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)

    override var passengers: List<Passenger>
        get() {
            val base64 = prefs.getString("passengers", null) ?: return emptyList()
            return serializer.passenger.list.decode(Base64.getDecoder().decode(base64))
        }
        set(value) {
            val encoded = serializer.passenger.list.encode(decoded = value)
            prefs
                .edit()
                .putString("passengers", Base64.getEncoder().encodeToString(encoded))
                .commit()
        }
}
