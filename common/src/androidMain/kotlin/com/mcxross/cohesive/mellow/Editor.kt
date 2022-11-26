@file: JvmName("AndroidEditor")

package com.mcxross.cohesive.mellow

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit

@Composable
actual fun Lines(
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
