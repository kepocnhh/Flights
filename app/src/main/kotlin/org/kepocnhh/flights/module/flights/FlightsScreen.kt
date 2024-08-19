package org.kepocnhh.flights.module.flights

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.kepocnhh.flights.App
import org.kepocnhh.flights.R
import org.kepocnhh.flights.module.settings.SettingsScreen
import org.kepocnhh.flights.util.compose.CircleButton
import org.kepocnhh.flights.util.compose.ColorIndication
import sp.ax.jc.animations.tween.fade.FadeVisibility
import sp.ax.jc.animations.tween.slide.SlideHVisibility
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
internal fun FlightsScreen() {
    val insets = WindowInsets.systemBars.asPaddingValues()
    val settingsState = remember { mutableStateOf(false) }
    val logics = App.logics<FlightsLogics>()
    val flights = logics.flights.collectAsState().value
    LaunchedEffect(Unit) {
        if (flights == null) logics.requestFlights()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(App.Theme.colors.background),
    ) {
        when {
            flights == null -> {
                // noop
            }
            flights.isEmpty() -> {
                // todo
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(
                        top = insets.calculateTopPadding() + 8.dp,
                        bottom = insets.calculateBottomPadding() + 8.dp + 64.dp + 16.dp
                    ),
                ) {
                    val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.US)
                    val timeFormat = SimpleDateFormat("hh:mm", Locale.US)
                    flights.forEach { flight ->
                        val date = Date(flight.created.inWholeMilliseconds)
                        item(key = flight.id) {
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .fillMaxWidth()
                                    .background(App.Theme.colors.secondary, RoundedCornerShape(16.dp))
                                    .padding(
                                        horizontal = 16.dp,
                                        vertical = 12.dp,
                                    ),
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    BasicText(
                                        text = "Вылет от ${dateFormat.format(date)}", // todo
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            color = App.Theme.colors.text,
                                        ),
                                    )
                                    BasicText(
                                        text = "Время создания ${timeFormat.format(date)}", // todo
                                        style = TextStyle(
                                            fontSize = 14.sp,
                                            color = App.Theme.colors.text,
                                        ),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
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
