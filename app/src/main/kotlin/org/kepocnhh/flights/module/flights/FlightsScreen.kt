package org.kepocnhh.flights.module.flights

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.kepocnhh.flights.App
import org.kepocnhh.flights.R
import org.kepocnhh.flights.entity.Flight
import org.kepocnhh.flights.module.passengers.NewPassengerLogics
import org.kepocnhh.flights.module.passengers.PassengersLogics
import org.kepocnhh.flights.module.settings.SettingsScreen
import org.kepocnhh.flights.util.compose.CircleButton
import org.kepocnhh.flights.util.compose.ColorIndication
import org.kepocnhh.flights.util.compose.consumeClicks
import sp.ax.jc.animations.tween.fade.FadeVisibility
import sp.ax.jc.animations.tween.slide.SlideHVisibility
import sp.ax.jc.clicks.clicks
import sp.ax.jc.clicks.onClick
import sp.ax.jc.dialogs.Dialog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

@Composable
internal fun FlightsScreen(
    toSettings: () -> Unit,
    toFlight: (UUID) -> Unit,
    toNewPassenger: () -> Unit,
) {
    val insets = WindowInsets.systemBars.asPaddingValues()
    val logics = App.logics<FlightsLogics>()
    val flights = logics.flights.collectAsState().value
    LaunchedEffect(Unit) {
        if (flights == null) logics.requestFlights()
    }
    LaunchedEffect(Unit) {
        Flights.events.collect { event ->
            when (event) {
                is Flights.Event.OnCreate -> logics.requestFlights()
                is Flights.Event.OnDeleteFlight -> logics.requestFlights()
            }
        }
    }
    val deleteFlightState = remember { mutableStateOf<Flight?>(null) }
    val deleteFlight = deleteFlightState.value
    if (deleteFlight != null) {
        Dialog(
            button = App.Theme.strings.yes to {
                logics.deleteFlight(flight = deleteFlight)
                deleteFlightState.value = null
            },
            onDismissRequest = {
                deleteFlightState.value = null
            },
            message = App.Theme.strings.deleteFlight,
        )
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .consumeClicks()
            .background(App.Theme.colors.background),
    ) {
        when {
            flights == null -> {
                // noop
            }
            flights.isEmpty() -> {
                BasicText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    text = App.Theme.strings.noFlights,
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = App.Theme.colors.text,
                        textAlign = TextAlign.Center,
                    ),
                )
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(
                        top = insets.calculateTopPadding() + 8.dp,
                        bottom = insets.calculateBottomPadding() + 8.dp + 64.dp + 16.dp,
                    ),
                ) {
                    val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.US)
                    val timeFormat = SimpleDateFormat("HH:mm", Locale.US)
                    flights.forEach { flight ->
                        val date = Date(flight.created.inWholeMilliseconds)
                        item(key = flight.id) {
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .fillMaxWidth()
                                    .background(
                                        App.Theme.colors.secondary,
                                        RoundedCornerShape(16.dp)
                                    )
                                    .clip(RoundedCornerShape(16.dp))
                                    .clicks(
                                        onClick = {
                                            println("[Flights]: on click flight: ${flight.id}") // todo
                                            toFlight(flight.id)
                                        },
                                        onLongClick = {
                                            deleteFlightState.value = flight
                                        },
                                    )
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
                                        text = String.format(App.Theme.strings.flightFrom, dateFormat.format(date)),
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            color = App.Theme.colors.text,
                                        ),
                                    )
                                    BasicText(
                                        text = String.format(App.Theme.strings.flightCreated, timeFormat.format(date)),
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
                    onClick = toSettings,
                )
                Spacer(modifier = Modifier.weight(1f))
                CircleButton(
                    indication = remember { ColorIndication(Color.White) },
                    color = App.Theme.colors.primary,
                    iconColor = App.Theme.colors.white,
                    iconId = R.drawable.plus,
                    contentDescription = "FlightsScreen:add",
                    onClick = toNewPassenger,
                )
            }
        }
    }
}
