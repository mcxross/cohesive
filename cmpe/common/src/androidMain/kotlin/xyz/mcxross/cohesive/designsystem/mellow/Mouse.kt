package xyz.mcxross.cohesive.designsystem.mellow

import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset

actual fun Modifier.pointerMoveFilter(
  onEnter: () -> Boolean,
  onExit: () -> Boolean,
  onMove: (Offset) -> Boolean
): Modifier = this

actual fun Modifier.cursorForHorizontalResize() = this
