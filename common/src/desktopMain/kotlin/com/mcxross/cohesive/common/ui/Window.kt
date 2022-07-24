package com.mcxross.cohesive.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPlacement
import com.mcxross.cohesive.common.view.View


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WindowButton(
    res: String,
    contentDescription: String,
    onHoverColor: Color = Color(0xFF6E6E6E),
    width: Dp = 54.dp,
    height: Dp = 30.dp,
    background: Color = MaterialTheme.colors.surface,
    tint: Color = contentColorFor(MaterialTheme.colors.surface),
    onClick: () -> Unit
) {
    var active by remember { mutableStateOf(false) }
    Box(modifier = Modifier.size(width, height)
        .background(color = if (active) onHoverColor else background)
        .clickable { onClick() }
        .onPointerEvent(PointerEventType.Enter) { active = true }
        .onPointerEvent(PointerEventType.Exit) { active = false }) {
        Icon(painterResource(res), contentDescription, modifier = Modifier.align(alignment = Alignment.Center), tint = tint)
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
    var maximize by remember { mutableStateOf(true) }
    WindowButton(if (maximize) "maximize_dark.svg" else "restore_dark.svg", "Restore Down, and Maximize window") {
        if (maximize) {
            WindowStateHolder.state.placement = WindowPlacement.Maximized
            maximize = false
        } else {
            WindowStateHolder.state.placement = WindowPlacement.Floating
            maximize = true
        }
    }
}

@Composable
fun WindowCloseButton() {
    WindowButton("close_dark.svg", "Close window", onHoverColor = Color.Red) {
        WindowStateHolder.isWindowOpen = false
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

@Composable
fun WindowListMenuButton() {
    WindowButton("listMenu_dark.svg", contentDescription = "Action List", width = 40.dp) {

    }
}

@Composable
fun SwitchView() {

    WindowButton("switchView_dark.svg", contentDescription = "Switch View", width = 40.dp) {

        if (WindowStateHolder.view == View.EXPLORER) {
            WindowStateHolder.view = View.WALLET
        } else {
            WindowStateHolder.view = View.EXPLORER
        }

    }
}

@Composable
fun DialogCloseButton() {
    WindowButton("closeSmall_dark.svg", contentDescription = "Close Dialog", width = 54.dp, height = 25.dp, onHoverColor = Color(0xFFC75450), background = Color(0xFFDB5860), tint = Color.White) {
        WindowStateHolder.isImportAccountOpen = false
    }
}