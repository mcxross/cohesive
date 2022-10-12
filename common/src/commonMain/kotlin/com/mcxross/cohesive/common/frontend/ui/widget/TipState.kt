package com.mcxross.cohesive.common.frontend.ui.widget

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned

/**
 * Creates a [TipState] that is remembered across compositions.
 *
 * Changes to the provided values for [initialIndex] will **not** result in the state being
 * recreated or changed in any way if it has already
 * been created.
 *
 * @param initialIndex the initial value for [TipState.currentTargetIndex]
 */
@Composable
fun rememberTipState(
    initialIndex: Int = 0,
): TipState {
    return remember {
        TipState(
            initialIndex = initialIndex,
        )
    }
}

/**
 * Modifier that marks Compose UI element as a target for [Tip]
 */
fun Modifier.tipTarget(
    state: TipState,
    index: Int,
    style: TipStyle = TipStyle.Default,
    content: @Composable BoxScope.() -> Unit,
): Modifier = onGloballyPositioned { coordinates ->
    state.targets[index] = TipTargets(
        index = index,
        coordinates = coordinates,
        style = style,
        content = content
    )
}

class TipState internal constructor(
    initialIndex: Int,
) {

    internal var targets = mutableStateMapOf<Int, TipTargets>()

    var currentTargetIndex by mutableStateOf(initialIndex)
        internal set

    val currentTarget: TipTargets?
        get() = targets[currentTargetIndex]
}