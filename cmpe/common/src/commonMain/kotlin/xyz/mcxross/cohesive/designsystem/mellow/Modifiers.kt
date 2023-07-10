package xyz.mcxross.cohesive.designsystem.mellow

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Indication
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.semantics.Role

fun Modifier.withoutWidthConstraints() = layout { measurable, constraints ->
  val placeable = measurable.measure(constraints.copy(maxWidth = Int.MAX_VALUE))
  layout(constraints.maxWidth, placeable.height) { placeable.place(0, 0) }
}

/** An overload of [Modifier.combinedClickable] */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Modifier.combinedClickableNoInteraction(
  enabled: Boolean = true,
  interactionSource: MutableInteractionSource? = null,
  indication: Indication?,
  onClickLabel: String? = null,
  role: Role? = null,
  onLongClickLabel: String? = null,
  onLongClick: (() -> Unit)? = null,
  onDoubleClick: (() -> Unit)? = null,
  onClick: () -> Unit,
) =
  composed(
    inspectorInfo =
      debugInspectorInfo {
        name = "combinedClickable"
        properties["enabled"] = enabled
        properties["onClickLabel"] = onClickLabel
        properties["role"] = role
        properties["onClick"] = onClick
        properties["onDoubleClick"] = onDoubleClick
        properties["onLongClick"] = onLongClick
        properties["onLongClickLabel"] = onLongClickLabel
      },
  ) {
    Modifier.combinedClickable(
      enabled = enabled,
      onClickLabel = onClickLabel,
      onLongClickLabel = onLongClickLabel,
      onLongClick = onLongClick,
      onDoubleClick = onDoubleClick,
      onClick = onClick,
      role = role,
      indication = indication,
      interactionSource = interactionSource ?: remember { MutableInteractionSource() },
    )
  }
