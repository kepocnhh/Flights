package org.kepocnhh.flights.module.passengers

import androidx.activity.compose.BackHandler
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
import org.kepocnhh.flights.util.compose.CircleButton
import org.kepocnhh.flights.util.compose.ColorIndication
import org.kepocnhh.flights.util.compose.consumeClicks
import sp.ax.jc.clicks.onClick
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

@Composable
internal fun PassengersScreen(
    flightId: UUID,
    onBack: () -> Unit,
    toNewPassenger: () -> Unit,
) {
    BackHandler(onBack = onBack)
    val insets = WindowInsets.systemBars.asPaddingValues()
    val logics = App.logics<PassengersLogics>()
    val passengers = logics.passengers.collectAsState().value
    LaunchedEffect(Unit) {
        if (passengers == null) logics.requestPassengers(flightId)
    }
    val newPassengerLogics = App.logics<NewPassengerLogics>()
    LaunchedEffect(Unit) {
        newPassengerLogics.events.collect { event ->
            when (event) {
                is NewPassengerLogics.Event.OnCreate -> {
                    logics.requestPassengers(flightId)
                }
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .consumeClicks()
            .background(App.Theme.colors.background),
    ) {
        when {
            passengers == null -> {
                // noop
            }
            passengers.isEmpty() -> {
                BasicText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    text = App.Theme.strings.noPassengers,
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
                    val timeFormat = SimpleDateFormat("hh:mm", Locale.US)
                    passengers.forEach { passenger ->
                        val date = Date(passenger.created.inWholeMilliseconds)
                        item(key = passenger.id) {
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .fillMaxWidth()
                                    .background(App.Theme.colors.secondary, RoundedCornerShape(16.dp))
                                    .clip(RoundedCornerShape(16.dp))
                                    .onClick {
                                        // todo
                                    }
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
                                        text = listOf(
                                            passenger.person.lastName,
                                            passenger.person.firstName,
                                            passenger.person.middleName,
                                        ).filter { it.isNotEmpty() }.joinToString(separator = " "),
                                        style = TextStyle(
                                            fontSize = 16.sp,
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
                Spacer(modifier = Modifier.weight(1f))
                CircleButton(
                    indication = remember { ColorIndication(Color.White) },
                    color = App.Theme.colors.primary,
                    iconColor = App.Theme.colors.white,
                    iconId = R.drawable.plus,
                    contentDescription = "PassengersScreen:add",
                    onClick = toNewPassenger,
                )
            }
        }
    }
}
