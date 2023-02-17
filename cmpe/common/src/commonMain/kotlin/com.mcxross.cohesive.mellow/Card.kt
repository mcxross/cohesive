package com.mcxross.cohesive.mellow

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A composable that displays a card with a default Mellow design card appearance.
 *
 * It's built on top of the Material Design Card component.
 *
 * @param modifier Modifier to be applied to the root element of the composable.
 * @param shape The shape to be applied to the corners of the card.
 * @param backgroundColor The background color of the card.
 * @param contentColor The color to be applied to text and other content in the card.
 * @param border The border to be applied to the card.
 * @param elevation The elevation of the card.
 * @param content The content to be displayed inside the card.
 */
@Composable
fun Card(
  modifier: Modifier,
  shape: Shape = RoundedCornerShape(15.dp),
  backgroundColor: Color = MaterialTheme.colors.surface,
  contentColor: Color = contentColorFor(backgroundColor),
  border: BorderStroke? = null,
  elevation: Dp = 1.dp,
  content: @Composable () -> Unit,
) =
  androidx.compose.material.Card(
    modifier = modifier,
    shape = shape,
    backgroundColor = backgroundColor,
    contentColor = contentColor,
    border = border,
    elevation = elevation,
  ) {
    content()
  }
