package xyz.mcxross.cohesive.ui.impl.view.md.markup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalUnitApi::class)
@Composable
fun Code(
  text: String,
  backgroundColor: Color,
  textColor: Color,
) {
  Column {
    Spacer(modifier = Modifier.height(10.dp))
    Box(
      modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(backgroundColor),
    ) {
      Box(modifier = Modifier.padding(10.dp)) {
        Text(
          text = text.replace("`", "").trim(),
          modifier = Modifier.wrapContentHeight().wrapContentWidth(),
          fontSize = TextUnit(13f, TextUnitType.Sp),
          color = textColor,
        )
      }
    }
  }
}

data class Code(
  var codeBlock: String,
) : Element
