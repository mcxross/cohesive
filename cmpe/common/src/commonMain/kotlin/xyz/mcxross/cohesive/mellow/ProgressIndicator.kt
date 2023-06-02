package xyz.mcxross.cohesive.mellow

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp

@Composable
internal fun ProgressIndicator(
  modifier: Modifier,
  size: Dp,
  onDraw: DrawScope.() -> Unit,
) {
  Canvas(
    modifier = modifier.progressSemantics().size(size).focusable(),
    onDraw = onDraw,
  )
}
