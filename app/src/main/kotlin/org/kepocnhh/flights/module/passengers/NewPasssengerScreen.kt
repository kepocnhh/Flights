package org.kepocnhh.flights.module.passengers

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.kepocnhh.flights.App
import org.kepocnhh.flights.entity.Person
import org.kepocnhh.flights.util.compose.consumeClicks
import sp.ax.jc.clicks.onClick

@Composable
private fun TextField(
    title: String,
    state: MutableState<String>,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        BasicText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            text = title,
            style = TextStyle(
                color = App.Theme.colors.text,
                fontSize = 14.sp,
            ),
        )
        Spacer(modifier = Modifier.height(8.dp))
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .background(App.Theme.colors.basement, RoundedCornerShape(16.dp))
                .padding(16.dp),
            value = state.value,
            onValueChange = {
                if (it.length < 32) {
                    state.value = it
                }
            },
            textStyle = TextStyle(
                color = App.Theme.colors.text,
                fontSize = 16.sp,
            ),
        )
    }
}

@Composable
internal fun NewPassengerScreen(
    onBack: () -> Unit,
) {
    BackHandler(onBack = onBack)
    val insets = WindowInsets.systemBars.asPaddingValues()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .consumeClicks()
            .background(App.Theme.colors.background),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(insets),
        ) {
            BasicText(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .wrapContentSize(),
                text = App.Theme.strings.newPassenger,
                style = TextStyle(
                    color = App.Theme.colors.text,
                    fontSize = 16.sp,
                ),
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(App.Theme.colors.secondary),
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 16.dp,
                                vertical = 8.dp,
                            ),
                    ) {
                        val lastNameState = remember { mutableStateOf("") }
                        val firstNameState = remember { mutableStateOf("") }
                        val meddleNameState = remember { mutableStateOf("") }
                        TextField(
                            title = App.Theme.strings.lastName,
                            state = lastNameState,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        TextField(
                            title = App.Theme.strings.firstName,
                            state = firstNameState,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        TextField(
                            title = App.Theme.strings.middleName,
                            state = meddleNameState,
                        )
                    }
                }
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(App.Theme.colors.secondary),
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 8.dp,
                    ),
            ) {
                BasicText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(App.Theme.colors.primary, RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .onClick {
                            // todo
                        }
                        .padding(horizontal = 16.dp)
                        .wrapContentSize(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = App.Theme.strings.create,
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp,
                    ),
                )
            }
        }
    }
}
