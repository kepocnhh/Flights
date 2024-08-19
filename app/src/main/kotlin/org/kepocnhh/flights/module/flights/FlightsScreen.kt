package org.kepocnhh.flights.module.flights

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.kepocnhh.flights.App
import org.kepocnhh.flights.R
import org.kepocnhh.flights.module.settings.SettingsScreen
import org.kepocnhh.flights.util.compose.CircleButton
import org.kepocnhh.flights.util.compose.ColorIndication
import sp.ax.jc.animations.tween.fade.FadeVisibility
import sp.ax.jc.animations.tween.slide.SlideHVisibility

@Composable
internal fun FlightsScreen() {
    val insets = WindowInsets.systemBars.asPaddingValues()
    val settingsState = remember { mutableStateOf(false) }
    val logics = App.logics<FlightsLogics>()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(App.Theme.colors.background),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(insets),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.BottomEnd),
            ) {
                CircleButton(
                    color = App.Theme.colors.background,
                    iconColor = App.Theme.colors.foreground,
                    iconId = R.drawable.gear,
                    contentDescription = "FlightsScreen:settings",
                    onClick = {
                        settingsState.value = true
                    },
                )
                Spacer(modifier = Modifier.weight(1f))
                CircleButton(
                    indication = remember { ColorIndication(Color.White) },
                    color = App.Theme.colors.primary,
                    iconColor = App.Theme.colors.white,
                    iconId = R.drawable.plus,
                    contentDescription = "FlightsScreen:add",
                    onClick = {
                        // todo
                    },
                )
            }
        }
        FadeVisibility(
            visible = settingsState.value,
        ) {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.75f)))
        }
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
