package com.mcxross.cohesive.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPlacement


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WindowButton(
    res: String,
    contentDescription: String,
    onHoverColor: Color = Color(0xFF6E6E6E),
    onClick: () -> Unit
) {
    var active by remember { mutableStateOf(false) }
    Box(modifier = Modifier.size(54.dp, 30.dp)
        .background(color = if (active) onHoverColor else MaterialTheme.colors.surface)
        .clickable { onClick() }
        .onPointerEvent(PointerEventType.Enter) { active = true }
        .onPointerEvent(PointerEventType.Exit) { active = false }) {
        Icon(painterResource(res), contentDescription, modifier = Modifier.align(alignment = Alignment.Center))
    }
}

@Composable
fun WindowMinimizeButton() {

    WindowButton("minimize_dark.svg", "Minimize window") {
        WindowStateHolder.state.isMinimized = !WindowStateHolder.state.isMinimized
    }
}

@Composable
fun WindowResizeButton() {
    var resized by remember { mutableStateOf(false) }
    WindowButton("restore_dark.svg", "Restore window") {
        if (!resized) {
            WindowStateHolder.state.placement = WindowPlacement.Fullscreen
            resized = true
        } else {
            WindowStateHolder.state.placement = WindowPlacement.Floating
            resized = false
        }
    }
}

@Composable
fun WindowCloseButton() {
    WindowButton("close_dark.svg", "Close window", onHoverColor = Color.Red) {

    }
}

@Composable
fun WindowButtons() {
    Row(modifier = Modifier.fillMaxSize()) {
        WindowMinimizeButton()
        WindowResizeButton()
        WindowCloseButton()
    }
}