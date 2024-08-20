package org.kepocnhh.flights.module.passengers

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.kepocnhh.flights.App
import org.kepocnhh.flights.R
import org.kepocnhh.flights.entity.Passenger
import org.kepocnhh.flights.module.flights.Flights
import org.kepocnhh.flights.util.compose.CircleButton
import org.kepocnhh.flights.util.compose.ColorIndication
import org.kepocnhh.flights.util.compose.consumeClicks
import org.kepocnhh.flights.util.showToast
import sp.ax.jc.clicks.clicks
import sp.ax.jc.dialogs.Dialog
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
    val context = LocalContext.current
    val insets = WindowInsets.systemBars.asPaddingValues()
    val logics = App.logics<PassengersLogics>()
    val loading = logics.loading.collectAsState().value
    val passengers = logics.passengers.collectAsState().value
    LaunchedEffect(Unit) {
        if (passengers == null) logics.requestPassengers(flightId)
    }
    LaunchedEffect(Unit) {
        Flights.events.collect { event ->
            when (event) {
                is Flights.Event.OnCreate -> {
                    logics.requestPassengers(flightId)
                }
                is Flights.Event.OnDeleteFlight -> onBack()
            }
        }
    }
    LaunchedEffect(Unit) {
        logics.events.collect { event ->
            when (event) {
                is PassengersLogics.Event.OnExport -> {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.putExtra(Intent.EXTRA_STREAM, event.file.absolutePath)
                    intent.type = "*/*"
                    val mimetypes = arrayOf(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    )
                    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes)
                    context.startActivity(Intent.createChooser(intent, null))
//                    context.showToast("file: ${event.file.absolutePath}") // todo
                }
            }
        }
    }
    val deletePassengerState = remember { mutableStateOf<Passenger?>(null) }
    val deletePassenger = deletePassengerState.value
    if (deletePassenger != null) {
        Dialog(
            button = App.Theme.strings.yes to {
                logics.deletePassenger(passenger = deletePassenger)
                deletePassengerState.value = null
            },
            onDismissRequest = {
                deletePassengerState.value = null
            },
            message = App.Theme.strings.deletePassenger,
            messageTextStyle = TextStyle(
                fontSize = 16.sp,
                color = App.Theme.colors.text,
            ),
        )
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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = insets.calculateTopPadding()),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                    ) {
                        Spacer(modifier = Modifier.width(64.dp))
                        BasicText(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f)
                                .wrapContentSize(),
                            text = App.Theme.strings.passengers,
                            style = TextStyle(
                                color = App.Theme.colors.text,
                                fontSize = 16.sp,
                            ),
                        )
                        if (passengers.isEmpty()) {
                            Spacer(modifier = Modifier.width(64.dp))
                        } else {
                            BasicText(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(64.dp)
                                    .wrapContentSize(),
                                text = passengers.size.toString(),
                                style = TextStyle(
                                    color = App.Theme.colors.text,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                ),
                            )
                        }
                    }
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(App.Theme.colors.secondary),
                    )
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(
                            top = 8.dp,
                            bottom = insets.calculateBottomPadding() + 8.dp + 64.dp + 16.dp,
                        ),
                    ) {
                        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.US)
                        val timeFormat = SimpleDateFormat("hh:mm", Locale.US)
                        passengers.forEach { passenger ->
                            val created = Date(passenger.created.inWholeMilliseconds)
                            val born = Date(passenger.born.inWholeMilliseconds)
                            item(key = passenger.id) {
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
                                            enabled = !loading,
                                            onClick = {
                                                // todo
                                            },
                                            onLongClick = {
                                                deletePassengerState.value = passenger
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
                                            text = listOf(
                                                passenger.person.lastName,
                                                passenger.person.firstName,
                                            ).filter { it.isNotBlank() }.joinToString(separator = " "),
                                            style = TextStyle(
                                                fontSize = 16.sp,
                                                color = App.Theme.colors.text,
                                            ),
                                        )
                                        if (passenger.person.middleName.isNotBlank()) {
                                            BasicText(
                                                text = passenger.person.middleName,
                                                style = TextStyle(
                                                    fontSize = 14.sp,
                                                    color = App.Theme.colors.text,
                                                ),
                                            )
                                        }
                                        BasicText(
                                            text = String.format(App.Theme.strings.born, dateFormat.format(born)),
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
                    enabled = !loading,
                    color = App.Theme.colors.background,
                    iconColor = App.Theme.colors.foreground,
                    iconId = R.drawable.download,
                    contentDescription = "PassengersScreen:export",
                    onClick = {
                        logics.exportPassengers(flightId = flightId)
                    },
                )
                Spacer(modifier = Modifier.weight(1f))
                CircleButton(
                    enabled = !loading,
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
