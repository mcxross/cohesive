package com.mcxross.cohesive.desktop

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.window.*
import com.mcxross.cohesive.common.common.SplashScreen
import com.mcxross.cohesive.common.openapi.IMainScreen
import com.mcxross.cohesive.common.utils.WindowStateHolder
import com.mcxross.cohesive.common.utils.getPreferredWindowSize
import com.mcxross.cohesive.desktop.utils.loadPluginsAsync
import kotlinx.coroutines.delay
import org.pf4j.PluginManager


fun main() = application {

    var isPerformingTask by remember { mutableStateOf(true) }
    //monitor this value
    var pluginManager: PluginManager = DefaultAsyncPluginManager()

    val state = rememberWindowState(
        placement = WindowPlacement.Floating,
        position = WindowPosition.Aligned(Alignment.Center),
        size = getPreferredWindowSize(800, 1000)
    )
    WindowStateHolder.state = state

    val currentPlugins by rememberUpdatedState {
        loadPluginsAsync({}, {
            it.loadPlugins()
            it.startPlugins()
            pluginManager = it
            isPerformingTask = false
        })
    }

    LaunchedEffect(true) {
        delay(3000)
        currentPlugins()
    }

    if (isPerformingTask) {

        Window(
            onCloseRequest = ::exitApplication,
            undecorated = true,
            resizable = false,
            state = WindowState(
                position = WindowPosition.Aligned(Alignment.Center),
                size = getPreferredWindowSize(400, 300),
            ),
        ) {
            SplashScreen()
        }

    } else {

        if (WindowStateHolder.isWindowOpen) {
            Window(
                onCloseRequest = ::exitApplication,
                undecorated = true,
                state = WindowStateHolder.state,
                icon = BitmapPainter(useResource("ic_launcher.png", ::loadImageBitmap)),
            ) {
                pluginManager.getExtensions(IMainScreen::class.java).forEach {
                    it.Show(this)
                }
            }
        }

    }
}
