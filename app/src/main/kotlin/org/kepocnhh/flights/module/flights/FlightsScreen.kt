package org.kepocnhh.flights.module.flights

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import org.kepocnhh.flights.App
import org.kepocnhh.flights.module.settings.SettingsScreen
import sp.ax.jc.animations.tween.slide.SlideHVisibility

@Composable
internal fun FlightsScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(App.Theme.colors.background),
    ) {
        val settingsState = remember { mutableStateOf(false) }
        SlideHVisibility(
            visible = settingsState.value,
        ) {
            SettingsScreen(
                onBack = {
                    settingsState.value = false
                },
            )
        }
    }
}
