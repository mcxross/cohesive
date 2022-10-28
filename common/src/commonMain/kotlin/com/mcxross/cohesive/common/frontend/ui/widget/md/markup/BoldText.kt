package com.mcxross.cohesive.common.frontend.ui.widget.md.markup

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun BoldText(
  text: String,
  color: Color,
) {
  Text(
    text = text,
    fontWeight = FontWeight.Bold,
    modifier = Modifier.padding(5.dp),
    color = color,
  )
}

data class BoldText(
  val text: String,
) : Element
