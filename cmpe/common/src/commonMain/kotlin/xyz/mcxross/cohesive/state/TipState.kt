package xyz.mcxross.cohesive.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import xyz.mcxross.cohesive.ui.impl.view.tip.TipTargets

class TipState
internal constructor(
  initialIndex: Int,
) {

  internal var targets = mutableStateMapOf<Int, TipTargets>()

  var currentTargetIndex by mutableStateOf(initialIndex)
    internal set

  val currentTarget: TipTargets?
    get() = targets[currentTargetIndex]
}
