package org.kepocnhh.flights

import android.app.Application
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import org.kepocnhh.flights.module.app.Colors
import org.kepocnhh.flights.module.app.Injection
import org.kepocnhh.flights.module.app.Strings
import org.kepocnhh.flights.module.app.ThemeState
import org.kepocnhh.flights.provider.Contexts
import org.kepocnhh.flights.provider.FinalLocals
import org.kepocnhh.flights.provider.FinalLoggers
import org.kepocnhh.flights.provider.FinalSerializer
import org.kepocnhh.flights.provider.Serializer
import sp.ax.jc.dialogs.DialogStyle
import sp.ax.jc.dialogs.LocalDialogStyle
import sp.ax.jc.squares.LocalSquaresStyle
import sp.ax.jc.squares.SquaresStyle
import sp.kx.logics.Logics
import sp.kx.logics.LogicsFactory
import sp.kx.logics.LogicsProvider
import sp.kx.logics.contains
import sp.kx.logics.get
import sp.kx.logics.remove

internal class App : Application() {
    object Theme {
        private val LocalColors = staticCompositionLocalOf<Colors> { error("No colors!") }
        private val LocalStrings = staticCompositionLocalOf<Strings> { error("No strings!") }

        val colors: Colors
            @Composable
            @ReadOnlyComposable
            get() = LocalColors.current

        val strings: Strings
            @Composable
            @ReadOnlyComposable
            get() = LocalStrings.current

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
            val strings = Strings.Ru // todo
            CompositionLocalProvider(
                LocalColors provides colors,
                LocalStrings provides strings,
                LocalDialogStyle provides DialogStyle(
                    background = colors.background,
                    foreground = colors.text,
                ),
                LocalSquaresStyle provides SquaresStyle(
                    color = colors.foreground,
                    squareSize = DpSize(width = 32.dp, height = 32.dp),
                    paddingOffset = DpOffset(x = 16.dp, y = 16.dp),
                    cornerRadius = 8.dp,
                    backgroundContext = Dispatchers.Default,
                ),
                content = content,
            )
        }
    }

    override fun onCreate() {
        super.onCreate()
        val serializer: Serializer = FinalSerializer
        _injection = Injection(
            contexts = Contexts(
                main = Dispatchers.Main,
                default = Dispatchers.Default,
            ),
            loggers = FinalLoggers,
            locals = FinalLocals(context = this, serializer = serializer),
        )
    }

    companion object {
        private var _injection: Injection? = null
        private val _logicsProvider = LogicsProvider(
            factory = object : LogicsFactory {
                override fun <T : Logics> create(type: Class<T>): T {
                    val injection = checkNotNull(_injection) { "No injection!" }
                    return type
                        .getConstructor(Injection::class.java)
                        .newInstance(injection)
                }
            },
        )

        @Composable
        inline fun <reified T : Logics> logics(label: String = T::class.java.name): T {
            val (contains, logic) = synchronized(App::class.java) {
                remember { _logicsProvider.contains<T>(label = label) } to _logicsProvider.get<T>(label = label)
            }
            DisposableEffect(Unit) {
                onDispose {
                    synchronized(App::class.java) {
                        if (!contains) {
                            _logicsProvider.remove<T>(label = label)
                        }
                    }
                }
            }
            return logic
        }
    }
}
