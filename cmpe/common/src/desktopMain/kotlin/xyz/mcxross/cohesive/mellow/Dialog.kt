package xyz.mcxross.cohesive.mellow

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberDialogState

@Composable
private fun Content(
  modifier: Modifier,
  content: @Composable () -> Unit,
) {
  Box(modifier = modifier) {
    content()
  }
}

@Composable
private fun BottomBar(
  modifier: Modifier,
  negativeText: String,
  neutralText: String,
  positiveText: String,
  onNegative: () -> Unit,
  onNeutral: () -> Unit,
  onPositive: () -> Unit,
  negativeEnable: Boolean,
  neutralEnable: Boolean,
  positiveEnable: Boolean,
) {
  Column(modifier = modifier) {
    Divider()
    Box(
      modifier = Modifier.fillMaxWidth().height(55.dp).padding(end = 5.dp),
    ) {
      Row(
        modifier = Modifier.fillMaxHeight().align(Alignment.CenterEnd),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        if (positiveText.isNotEmpty()) {
          Button(
            onClick = onPositive,
            text = positiveText,
            enabled = positiveEnable,
            modifier = Modifier.height(35.dp).align(Alignment.CenterVertically),
          )
        }

        if (neutralText.isNotEmpty()) {
          Button(
            onClick = onNeutral,
            text = neutralText,
            enabled = neutralEnable,
            modifier = Modifier.height(35.dp).align(Alignment.CenterVertically),
          )
        }

        if (negativeText.isNotEmpty()) {
          Button(
            onClick = onNegative,
            text = negativeText,
            enabled = negativeEnable,
            modifier = Modifier.height(35.dp).align(Alignment.CenterVertically),
          )
        }

      }
    }
  }
}

@Composable
fun Dialog(
  onClose: () -> Unit,
  text: String,
  negativeText: String = "",
  neutralText: String = "",
  positiveText: String = "",
  onNegative: () -> Unit = {},
  onNeutral: () -> Unit = {},
  onPositive: () -> Unit = {},
  negativeEnable: Boolean = true,
  neutralEnable: Boolean = true,
  positiveEnable: Boolean = true,
  width: Dp = 400.dp,
  height: Dp = 300.dp,
  content: @Composable () -> Unit,
) {

  var isDialogOpen by remember { mutableStateOf(false) }
  Dialog(
    onCloseRequest = { isDialogOpen = false },
    undecorated = true,
    resizable = false,
    state = rememberDialogState(
      position = WindowPosition(Alignment.Center),
      width = width,
      height = height,
    ),
  ) {
    Surface(
      modifier = Modifier.fillMaxSize(),
      contentColor = contentColorFor(MaterialTheme.colors.surface),
    ) {

      Box(modifier = Modifier.fillMaxSize()) {
        Content(
          Modifier.matchParentSize().align(Alignment.Center).padding(top = 30.dp, bottom = 57.dp),
          content,
        )
        TopMinBar(onClose = onClose, text = text, modifier = Modifier.align(Alignment.TopStart))
        BottomBar(
          modifier = Modifier.fillMaxWidth().align(Alignment.BottomStart),
          negativeText,
          neutralText,
          positiveText,
          onNegative,
          onNeutral,
          onPositive,
          negativeEnable,
          neutralEnable,
          positiveEnable,
        )
      }

    }
  }

}
