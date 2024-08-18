package org.kepocnhh.flights.module.app

import androidx.compose.ui.graphics.Color

internal sealed interface Colors {
    val primary: Color
    val black: Color
    val white: Color
    val background: Color
    val foreground: Color
    val secondary: Color
    val text: Color

    enum class Type {
        Auto,
        Dark,
        Light,
    }

    data object Light : Colors {
        override val primary = Color(0xff1E88E5)
        override val white = Color(0xffffffff)
        override val foreground = Color(0xff1f1f1f)
        override val secondary = Color(0xffdfdfdf)
        override val background = Color(0xffefefef)
        override val black = Color(0xff000000)
        override val text = black
    }

    data object Dark : Colors {
        override val primary = Color(0xff1E88E5)
        override val black = Color(0xff000000)
        override val background = Color(0xff1f1f1f)
        override val secondary = Color(0xff2f2f2f)
        override val foreground = Color(0xffefefef)
        override val white = Color(0xffffffff)
        override val text = white
    }
}
