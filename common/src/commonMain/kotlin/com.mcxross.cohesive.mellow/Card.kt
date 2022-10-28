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

@Composable
fun Card(
  modifier: Modifier,
  shape: Shape = RoundedCornerShape(15.dp),
  backgroundColor: Color = MaterialTheme.colors.surface,
  contentColor: Color = contentColorFor(backgroundColor),
  border: BorderStroke? = null,
  elevation: Dp = 1.dp,
  content: @Composable () -> Unit,
) = androidx.compose.material.Card(
  modifier = modifier,
  shape = shape,
  backgroundColor = backgroundColor,
  contentColor = contentColor,
  border = border,
  elevation = elevation,
) { content() }
