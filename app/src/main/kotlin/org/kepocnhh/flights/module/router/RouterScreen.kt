package org.kepocnhh.flights.module.router

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.kepocnhh.flights.App
import org.kepocnhh.flights.module.flights.FlightsScreen
import org.kepocnhh.flights.module.passengers.NewPassengerScreen
import org.kepocnhh.flights.module.passengers.PassengersScreen
import org.kepocnhh.flights.module.settings.SettingsScreen
import sp.ax.jc.animations.tween.fade.FadeVisibility
import sp.ax.jc.animations.tween.slide.SlideHVisibility
import java.util.UUID

@Composable
internal fun RouterScreen() {
    val settingsState = remember { mutableStateOf(false) }
    val flightState = remember { mutableStateOf<UUID?>(null) }
    val newPassengerState = remember { mutableStateOf<UUID?>(null) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(App.Theme.colors.background),
    ) {
        FlightsScreen(
            toSettings = {
                settingsState.value = true
            },
            toFlight = { id ->
                println("[Router]: to flight: $id") // todo
                flightState.value = id
            },
            toNewPassenger = {
                newPassengerState.value = UUID.randomUUID()
            },
        )
        FadeVisibility(settingsState.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.75f)),
            )
        }
        SlideHVisibility(settingsState.value) {
            SettingsScreen(
                onBack = {
                    settingsState.value = false
                },
            )
        }
        FadeVisibility(flightState.value != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.75f)),
            )
        }
        SlideHVisibility(
            visible = flightState.value != null,
        ) {
            val flightId = remember { mutableStateOf(flightState.value!!) }.value
            println("[Router]: to passengers: $flightId") // todo
            PassengersScreen(
                flightId = flightId,
                onBack = {
                    flightState.value = null
                },
                toNewPassenger = {
                    newPassengerState.value = flightId
                },
            )
        }
        FadeVisibility(newPassengerState.value != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.75f)),
            )
        }
        SlideHVisibility(
            visible = newPassengerState.value != null,
        ) {
            val flightId = remember { mutableStateOf(newPassengerState.value!!) }.value
            NewPassengerScreen(
                flightId = flightId,
                onBack = {
                    newPassengerState.value = null
                },
                onPassenger = {
                    newPassengerState.value = null
                    flightState.value = flightId
                },
            )
        }
    }
}
