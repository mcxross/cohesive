package xyz.mcxross.cohesive.ui.impl.view.tip

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import xyz.mcxross.cohesive.state.TipState

class TipScope(
  private val state: TipState,
) {

  /** Modifier that marks Compose UI element as a target for [Tip] */
  fun Modifier.tipTarget(
    index: Int,
    style: TipStyle = TipStyle.Default,
    content: @Composable BoxScope.() -> Unit,
  ): Modifier =
    this@tipTarget.tipTarget(
      state = state,
      index = index,
      style = style,
      content = content,
    )
}
