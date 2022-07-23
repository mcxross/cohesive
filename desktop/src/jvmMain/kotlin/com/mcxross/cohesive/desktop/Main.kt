package com.mcxross.cohesive.desktop

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.window.*
import com.mcxross.cohesive.common.ui.WindowStateHolder
import com.mcxross.cohesive.common.view.SplashScreen
import com.mcxross.cohesive.common.utils.getPreferredWindowSize
import com.mcxross.cohesive.common.view.MainScreen
import kotlinx.coroutines.delay

fun main() = application {

    var isPerformingTask by remember { mutableStateOf(true) }

    val state = rememberWindowState(
        placement = WindowPlacement.Floating,
        position = WindowPosition.Aligned(Alignment.Center),
        size = getPreferredWindowSize(800, 1000)
    )

    WindowStateHolder.state = state

    LaunchedEffect(Unit) {
        delay(3000)
        isPerformingTask = false
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

        Window(
            onCloseRequest = ::exitApplication,
            undecorated = true,
            state = WindowStateHolder.state,
            icon = BitmapPainter(useResource("ic_launcher.png", ::loadImageBitmap)),
        ) {
            MainScreen()
        }
    }
}