package xyz.mcxross.cohesive.ui.impl.view

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import xyz.mcxross.cohesive.state.TipState
import xyz.mcxross.cohesive.ui.impl.view.tip.Tip
import xyz.mcxross.cohesive.ui.impl.view.tip.TipScope
import xyz.mcxross.cohesive.ui.impl.view.tip.rememberTipState

@Composable
fun TipScaffold(
  tip: Boolean,
  onTip: () -> Unit,
  modifier: Modifier = Modifier,
  state: TipState = rememberTipState(),
  content: @Composable TipScope.() -> Unit,
) {
  val scope = remember(state) { TipScope(state) }

  Box(modifier) {
    scope.content()

    if (tip) {
      Tip(
        state = state,
        onShowCaseCompleted = onTip,
      )
    }
  }
}
