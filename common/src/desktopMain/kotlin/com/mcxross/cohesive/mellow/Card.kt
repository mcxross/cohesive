@file: JvmName("MellowCard")

package com.mcxross.cohesive.mellow

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Card(
  modifier: Modifier = Modifier,
  width: Dp,
  height: Dp,
  hoverBehavior: Boolean = true,
  shape: Shape = RoundedCornerShape(15.dp),
  backgroundColor: Color = MaterialTheme.colors.surface,
  contentColor: Color = contentColorFor(backgroundColor),
  border: BorderStroke? = null,
  elevation: Dp = 1.dp,
  content: @Composable () -> Unit,
) {
  if (hoverBehavior) {
    var currentState by remember { mutableStateOf(CardState.FLAT) }
    val transition = updateTransition(currentState)

    val widthAnim by transition.animateDp { state ->
      when (state) {
        CardState.FLAT -> width
        CardState.RAISED -> width + 5.dp
      }
    }

    val heightAnim by transition.animateDp { state ->
      when (state) {
        CardState.FLAT -> height
        CardState.RAISED -> height + 5.dp
      }
    }

    val elevationAnim by transition.animateDp { state ->
      when (state) {
        CardState.FLAT -> 1.dp
        CardState.RAISED -> 4.dp
      }
    }

    androidx.compose.material.Card(
      modifier = modifier
        .size(widthAnim, heightAnim)
        .onPointerEvent(PointerEventType.Enter) { currentState = CardState.RAISED }
        .onPointerEvent(PointerEventType.Exit) { currentState = CardState.FLAT },
      shape = shape,
      backgroundColor = backgroundColor,
      contentColor = contentColor,
      border = border,
      elevation = elevationAnim,
      content = content,
    )
  } else {
    androidx.compose.material.Card(
      modifier = modifier,
      shape = shape,
      backgroundColor = backgroundColor,
      contentColor = contentColor,
      border = border,
      elevation = elevation,
    ) { content() }
  }
}
