package org.kepocnhh.flights.util.compose

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
internal fun CircleButton(
    enabled: Boolean = true,
    indication: Indication = LocalIndication.current,
    elevation: Dp = 8.dp,
    size: Dp = 64.dp,
    color: Color,
    iconSize: Dp = 24.dp,
    iconColor: Color,
    @DrawableRes iconId: Int,
    contentDescription: String,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val onClickState = rememberUpdatedState(onClick)
    val elevationTarget = remember { mutableFloatStateOf(elevation.value) }
    val elevationActual = animateFloatAsState(
        targetValue = elevationTarget.floatValue,
        label = "CircleButton:$contentDescription",
    ).value.dp
    Box(
        modifier = Modifier
            .size(size)
            .shadow(
                elevation = elevationActual,
                shape = RoundedCornerShape(size / 2),
            )
            .background(color, RoundedCornerShape(size / 2))
            .indication(interactionSource, indication)
            .pointerInput(interactionSource, enabled) {
                detectTapGestures(
                    onPress = { offset ->
                        val press = PressInteraction.Press(offset)
                        interactionSource.emit(press)
                        elevationTarget.floatValue = elevation.value / 2
                        if (tryAwaitRelease()) {
                            interactionSource.emit(PressInteraction.Release(press))
                            if (enabled) onClickState.value()
                        } else {
                            interactionSource.emit(PressInteraction.Cancel(press))
                        }
                        elevationTarget.floatValue = elevation.value
                    },
                )
            },
    ) {
        Image(
            modifier = Modifier
                .size(iconSize)
                .align(Alignment.Center),
            painter = painterResource(id = iconId),
            contentDescription = contentDescription,
            colorFilter = ColorFilter.tint(iconColor),
        )
    }
}
