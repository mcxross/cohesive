@file: JvmName("AndroidEditor")

package xyz.mcxross.cohesive.designsystem.mellow

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit

@Composable
actual fun EditorMap(
  textLines: TextLines,
  fontSize: TextUnit,
) {

}

@Composable
actual fun TextField(
  text: String,
  isCode: Boolean,
  modifier: Modifier,
  fontSize: TextUnit,
  onScroll: (Float) -> Unit,
) {

}
