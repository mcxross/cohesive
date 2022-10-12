package com.mcxross.cohesive.common.frontend.ui.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun TipScaffold(
    tip: Boolean,
    onTip: () -> Unit,
    modifier: Modifier = Modifier,
    state: TipState = rememberTipState(),
    content: @Composable TipScope.() -> Unit,
) {
    val scope = remember(state) {
        TipScope(state)
    }

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

class TipScope(
    private val state: TipState,
) {

    /**
     * Modifier that marks Compose UI element as a target for [Tip]
     */
    fun Modifier.tipTarget(
        index: Int,
        style: TipStyle = TipStyle.Default,
        content: @Composable BoxScope.() -> Unit,
    ): Modifier = this@tipTarget.tipTarget(
        state = state,
        index = index,
        style = style,
        content = content,
    )
}