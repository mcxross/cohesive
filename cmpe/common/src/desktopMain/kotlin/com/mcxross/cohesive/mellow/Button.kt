@file: JvmName("MellowButton")

package com.mcxross.cohesive.mellow

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WindowButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  icon: Painter,
  contentDescription: String = "",
  iconTint: Color = contentColorFor(MaterialTheme.colors.surface),
  iconModifier: Modifier = Modifier,
  enabled: Boolean = true,
  onHoverColor: Color = Color(0xFF6E6E6E),
  background: Color = MaterialTheme.colors.surface,
  width: Dp = 54.dp,
  height: Dp = 30.dp,
  content: @Composable() (BoxScope.() -> Unit)? = null,
) {
  var active by remember { mutableStateOf(false) }
  Box(
    modifier = modifier.size(width, height)
      .background(color = if (active) onHoverColor else background)
      .clickable { onClick() }
      .onPointerEvent(PointerEventType.Enter) { active = true }
      .onPointerEvent(PointerEventType.Exit) { active = false },
  ) {
    if (content == null) {
      Icon(
        icon,
        contentDescription,
        modifier = iconModifier.align(alignment = Alignment.Center),
        tint = iconTint,
      )
    } else {
      content()
    }
  }
}
