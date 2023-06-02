package xyz.mcxross.cohesive.mellow

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonElevation
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Button(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  text: String = "",
  contentDescription: String? = text,
  icon: Painter? = null,
  iconTint: Color = MaterialTheme.colors.onSurface,
  iconModifier: Modifier = Modifier,
  enabled: Boolean = true,
  interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
  elevation: ButtonElevation? = ButtonDefaults.elevation(
    defaultElevation = 0.dp,
    hoveredElevation = 1.dp,
  ),
  shape: Shape = RoundedCornerShape(8.dp),
  border: BorderStroke? = BorderStroke(
    1.dp, MellowTheme.getColors().primaryVariant,
  ),
  colors: ButtonColors = ButtonDefaults.buttonColors(),
  contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
  content: @Composable (RowScope.() -> Unit)? = null,
) = Button(
  onClick = onClick,
  modifier = modifier,
  enabled = enabled,
  interactionSource = interactionSource,
  elevation = elevation,
  shape = shape,
  border = border,
  colors = colors,
  contentPadding = contentPadding,
) {
  if (content == null) {
    if (icon != null) {
      Icon(
        painter = icon,
        contentDescription = contentDescription,
        tint = iconTint,
        modifier = iconModifier,
      )
    }
    if (text.isNotEmpty()) {
      Text(
        text = text,
        fontWeight = FontWeight.W300,
        color = if (enabled) Color.White else contentColorFor(MaterialTheme.colors.surface),
      )
    }
  } else {
    content()
  }

}


@Composable
fun OutlinedButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  text: String = "",
  contentDescription: String? = text,
  icon: Painter? = null,
  iconTint: Color = MaterialTheme.colors.onSurface,
  iconModifier: Modifier = Modifier,
  enabled: Boolean = true,
  interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
  elevation: ButtonElevation? = ButtonDefaults.elevation(
    defaultElevation = 0.dp,
    hoveredElevation = 1.dp,
  ),
  shape: Shape = RoundedCornerShape(8.dp),
  border: BorderStroke? = ButtonDefaults.outlinedBorder,
  colors: ButtonColors = ButtonDefaults.outlinedButtonColors(),
  contentPadding: PaddingValues = PaddingValues(top = 2.dp, bottom = 2.dp),
  content: @Composable (RowScope.() -> Unit)? = null,
) = Button(
  onClick = onClick,
  modifier = modifier,
  text = text,
  icon = icon,
  contentDescription = contentDescription,
  iconTint = iconTint,
  iconModifier = iconModifier,
  enabled = enabled,
  interactionSource = interactionSource,
  elevation = elevation,
  shape = shape,
  border = border,
  colors = colors,
  contentPadding = contentPadding,
  content = content,
)

enum class TABBEDBUTTONTYPE {
  TEXT, ICON, TEXT_ICON
}

@Composable
private fun TabIndicator(
  indicatorWidth: Dp,
  indicatorOffset: Dp,
  indicatorColor: Color,
) = Box(
  modifier = Modifier
    .fillMaxHeight()
    .width(width = indicatorWidth)
    .offset(x = indicatorOffset)
    .clip(
      shape = CircleShape,
    )
    .background(
      color = indicatorColor,
    ),
)


@Composable
private fun TabItem(
  isSelected: Boolean,
  onClick: () -> Unit,
  tabWidth: Dp,
  text: String? = null,
  icon: Painter? = null,
  tabbedbuttontype: TABBEDBUTTONTYPE = TABBEDBUTTONTYPE.TEXT
) {

  when (tabbedbuttontype) {
    TABBEDBUTTONTYPE.TEXT -> {
      val tabTextColor: Color by animateColorAsState(
        targetValue = if (isSelected) {
          White
        } else {
          Black
        },
        animationSpec = tween(easing = LinearEasing),
      )
      if (text != null) {
        Text(
          modifier = Modifier
            .clip(CircleShape)
            .clickable {
              onClick()
            }
            .width(tabWidth)
            .padding(
              vertical = 8.dp,
              horizontal = 12.dp,
            ),
          text = text,
          fontSize = 12.sp,
          color = tabTextColor,
          textAlign = TextAlign.Center,
        )
      }
    }

    TABBEDBUTTONTYPE.ICON -> {

      if (icon != null) {
        Icon(
          icon,
          contentDescription = text,
          modifier = Modifier
            .width(tabWidth)
            .padding(
              vertical = 8.dp,
              horizontal = 12.dp,
            ).clickable {
              onClick()
            },
          tint = contentColorFor(MaterialTheme.colors.surface),
        )
      }

    }

    TABBEDBUTTONTYPE.TEXT_ICON -> {}
  }
}

@Composable
fun TabbedButton(
  selectedItemIndex: Int,
  items: List<String>,
  modifier: Modifier = Modifier,
  tabWidth: Dp = 80.dp,
  onClick: (index: Int) -> Unit,
) {
  val indicatorOffset: Dp by animateDpAsState(
    targetValue = tabWidth * selectedItemIndex,
    animationSpec = tween(easing = LinearEasing),
  )

  Box(
    modifier = modifier
      .clip(CircleShape)
      .background(MellowTheme.getColors().onSurface)
      .height(intrinsicSize = IntrinsicSize.Min),
  ) {
    TabIndicator(
      indicatorWidth = tabWidth,
      indicatorOffset = indicatorOffset,
      indicatorColor = MellowTheme.getColors().primary,
    )
    Row(
      horizontalArrangement = Arrangement.Center,
      modifier = Modifier.clip(CircleShape),
    ) {
      items.mapIndexed { index, text ->
        val isSelected = index == selectedItemIndex
        TabItem(
          isSelected = isSelected,
          onClick = {
            onClick(index)
          },
          tabWidth = tabWidth,
          text = text,
        )
      }
    }
  }
}

@Composable
fun TabbedButtonIcon(
  selectedItemIndex: Int,
  items: List<Pair<String, String>>,
  modifier: Modifier = Modifier,
  tabWidth: Dp = 50.dp,
  onClick: (index: Int) -> Unit,
) {

  val indicatorOffset: Dp by animateDpAsState(
    targetValue = tabWidth * selectedItemIndex,
    animationSpec = tween(easing = LinearEasing),
  )

  Box(
    modifier = modifier
      .clip(CircleShape)
      .background(MellowTheme.getColors().background)
      .height(intrinsicSize = IntrinsicSize.Min),
  ) {
    TabIndicator(
      indicatorWidth = tabWidth,
      indicatorOffset = indicatorOffset,
      indicatorColor = MellowTheme.getColors().primary,
    )
    Row(
      horizontalArrangement = Arrangement.Center,
      modifier = Modifier.clip(CircleShape),
    ) {
      items.mapIndexed { index, item ->
        val isSelected = index == selectedItemIndex
        TabItem(
          isSelected = isSelected,
          onClick = {
            onClick(index)
          },
          tabWidth = tabWidth,
          icon = painterResource(item.first),
          text = item.second,
          tabbedbuttontype = TABBEDBUTTONTYPE.ICON,
        )
      }
    }
  }

}
