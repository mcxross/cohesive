package com.mcxross.cohesive.common.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
        Icon(
            painterResource(res),
            contentDescription,
            modifier = Modifier.align(alignment = Alignment.Center),
            tint = tint
        )
    }
}
