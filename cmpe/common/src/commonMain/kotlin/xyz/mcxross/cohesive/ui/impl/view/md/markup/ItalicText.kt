package xyz.mcxross.cohesive.ui.impl.view.md.markup

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp

@Composable
fun ItalicText(
  text: String,
  color: Color,
) {
  Text(
    text = text,
    fontStyle = FontStyle.Italic,
    modifier = Modifier.padding(5.dp),
    color = color,
  )
}

data class ItalicText(
  val text: String,
) : Element
