package xyz.mcxross.cohesive.common.frontend.impl.ui.widget.md.markup

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Space(
  modifier: Modifier = Modifier,
) {
  Spacer(modifier = modifier.fillMaxWidth().height(5.dp))
}

class Space : Element
