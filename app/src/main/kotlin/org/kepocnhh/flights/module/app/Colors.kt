package org.kepocnhh.flights.module.app

import androidx.compose.ui.graphics.Color

internal sealed class Colors(
    val primary: Color,
    val black: Color,
    val white: Color,
    val background: Color,
    val foreground: Color,
) {

    enum class Type {
        Auto,
        Dark,
        Light,
    }

    data object Light : Colors(
        primary = Color(0xff1E88E5),
        white = Color(0xffffffff),
        foreground = Color(0xff1f1f1f),
        background = Color(0xffefefef),
        black = Color(0xff000000),
    )

    data object Dark : Colors(
        primary = Color(0xff1E88E5),
        black = Color(0xff000000),
        background = Color(0xff1f1f1f),
        foreground = Color(0xffefefef),
        white = Color(0xffffffff),
    )
}
