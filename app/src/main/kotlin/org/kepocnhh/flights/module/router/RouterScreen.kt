package org.kepocnhh.flights.module.router

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import org.kepocnhh.flights.App
import org.kepocnhh.flights.module.flights.FlightsScreen
import org.kepocnhh.flights.module.settings.SettingsScreen
import sp.ax.jc.animations.tween.slide.SlideHVisibility
import java.util.UUID

@Composable
internal fun RouterScreen() {
    val settingsState = remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(App.Theme.colors.background),
    ) {
        FlightsScreen(
            toSettings = {
                settingsState.value = true
            },
            toFlight = { id: UUID? ->
                TODO("RouterScreen:toFlight: $id")
            },
        )
        SlideHVisibility(settingsState.value) {
            SettingsScreen(
                onBack = {
                    settingsState.value = false
                },
            )
        }
    }
}
