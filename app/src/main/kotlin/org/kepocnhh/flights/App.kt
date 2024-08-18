package org.kepocnhh.flights

import android.app.Application
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import org.kepocnhh.flights.module.app.Colors
import org.kepocnhh.flights.module.app.ThemeState

internal class App : Application() {
    object Theme {
        private val LocalColors = staticCompositionLocalOf<Colors> { error("No colors!") }

        val colors: Colors
            @Composable
            @ReadOnlyComposable
            get() = LocalColors.current

        @Composable
        private fun getColors(colorsType: Colors.Type): Colors {
            return when (colorsType) {
                Colors.Type.Auto -> if (isSystemInDarkTheme()) Colors.Dark else Colors.Light
                Colors.Type.Dark -> Colors.Dark
                Colors.Type.Light -> Colors.Light
            }
        }

        @Composable
        fun Composition(
            themeState: ThemeState,
            content: @Composable () -> Unit,
        ) {
            val colors = getColors(themeState.colorsType)
            CompositionLocalProvider(
                LocalColors provides colors,
                content = content,
            )
        }
    }

    override fun onCreate() {
        super.onCreate()
        // todo
    }
}
