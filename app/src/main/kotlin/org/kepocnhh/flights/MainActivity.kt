package org.kepocnhh.flights

import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import org.kepocnhh.flights.module.app.Colors
import org.kepocnhh.flights.module.app.ThemeState
import org.kepocnhh.flights.module.flights.FlightsScreen

internal class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = ComposeView(this)
        setContentView(view)
        view.setContent {
            val themeState = ThemeState(colorsType = Colors.Type.Light) // todo
            App.Theme.Composition(themeState = themeState) {
                BackHandler(onBack = ::finish)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(App.Theme.colors.background),
                ) {
                    FlightsScreen()
                }
            }
        }
    }
}
