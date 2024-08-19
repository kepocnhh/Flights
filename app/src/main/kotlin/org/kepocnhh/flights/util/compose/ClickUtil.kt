package org.kepocnhh.flights.util.compose

import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.debugInspectorInfo

internal fun Modifier.consumeClicks(): Modifier {
    return composed(
        inspectorInfo = debugInspectorInfo {
            name = "consumeClicks"
        },
        factory = {
            val interactionSource = remember { MutableInteractionSource() }
            Modifier.indication(interactionSource = interactionSource, indication = null)
                .pointerInput(Unit, {})
        },
    )
}
