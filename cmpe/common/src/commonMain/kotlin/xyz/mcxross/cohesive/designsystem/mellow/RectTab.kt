package xyz.mcxross.cohesive.designsystem.mellow

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RectTab(
  text: String,
  active: Boolean,
  onActivate: () -> Unit,
  onDoubleClick: (() -> Unit)? = null,
  onClose: (() -> Unit?)? = null,
) =
  Surface(
    color =
      if (active) {
        MaterialTheme.colors.background
      } else {
        Color.Transparent
      },
  ) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
      modifier =
        Modifier.combinedClickableNoInteraction(
            interactionSource = interactionSource,
            indication = null,
            onClick = onActivate,
            onDoubleClick = onDoubleClick,
          )
          .padding(4.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Text(
        text = text,
        color = LocalContentColor.current,
        fontSize = 12.sp,
        modifier = Modifier.padding(horizontal = 4.dp),
        maxLines = 1,
      )

      if (onClose != null) {
        Icon(
          imageVector = Icons.Default.Close,
          tint = LocalContentColor.current,
          contentDescription = "Close",
          modifier = Modifier.size(24.dp).padding(4.dp).clickable { onClose() },
        )
      } else {
        Box(
          modifier = Modifier.size(24.dp, 24.dp).padding(4.dp),
        )
      }
    }
  }
