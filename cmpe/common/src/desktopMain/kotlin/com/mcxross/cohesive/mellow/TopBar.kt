package com.mcxross.cohesive.mellow

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.WindowScope

@Composable
fun WindowScope.TopBar(
  onClose: () -> Unit,
  onRestore: () -> Unit,
  onMinimize: () -> Unit,
  modifier: Modifier = Modifier,
  icon: Painter,
  iconModifier: Modifier = Modifier,
  iconDescription: String = "",
  closeIcon: Painter = painterResource("close_dark.svg"),
  closeIconModifier: Modifier = Modifier,
  closeIconDescription: String = "Close window",
  restoreIcon: Painter,
  restoreIconModifier: Modifier = Modifier,
  restoreIconDescription: String = "Restore Down, and Maximize window",
  minimizeIcon: Painter = painterResource("minimize_dark.svg"),
  minimizeIconModifier: Modifier = Modifier,
  minimizeIconDescription: String = "Minimize window",
  menuContent: @Composable (RowScope.() -> Unit)? = null,
  content: @Composable (RowScope.() -> Unit)? = null,
) = WindowDraggableArea {
  Column(
    modifier = modifier.fillMaxWidth().height(30.dp),
  ) {
    Row(
      modifier = Modifier.fillMaxSize().padding(start = 10.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
    ) {
      Row(
        modifier = Modifier.fillMaxHeight().wrapContentWidth(),
      ) {
        androidx.compose.foundation.Image(
          painter = icon,
          contentDescription = iconDescription,
          modifier = iconModifier.size(25.dp).align(Alignment.CenterVertically),
        )
        Row(
          modifier = Modifier.offset(x = 5.dp).align(Alignment.CenterVertically),
        ) {
          menuContent?.let { it() }
        }
      }
      Row(
        modifier = Modifier.fillMaxHeight(),
      ) {
        content?.let { it() }
      }
      Row(
        modifier = Modifier.fillMaxHeight().width(162.dp),
      ) {
        WindowButton(
          onClick = { onMinimize() },
          modifier = minimizeIconModifier,
          icon = minimizeIcon,
          contentDescription = minimizeIconDescription,
        )
        WindowButton(
          onClick = { onRestore() },
          modifier = restoreIconModifier,
          icon = restoreIcon,
          contentDescription = restoreIconDescription,
        )
        WindowButton(
          onClick = { onClose() },
          modifier = closeIconModifier,
          icon = closeIcon,
          contentDescription = closeIconDescription,
          onHoverColor = Color.Red,
        )
      }
    }
  }
}


@Composable
fun TopMinBar(
  onClose: () -> Unit,
  modifier: Modifier,
  text: String? = "",
  icon: Painter? = null,
  iconDescription: String? = text,
  iconModifier: Modifier = Modifier,
  iconAlignment: Alignment = Alignment.Center,
  iconContentScale: ContentScale = ContentScale.Fit,
  iconAlpha: Float = DefaultAlpha,
  iconColorFilter: ColorFilter? = null,
  content: @Composable() (BoxScope.() -> Unit)? = null
) = Column(
  modifier = modifier.fillMaxWidth().height(30.dp),
) {
  Box(
    modifier = Modifier.fillMaxWidth().height(29.5.dp).padding(start = 8.dp),
  ) {
    if (content == null) {
      Row(modifier = Modifier.fillMaxSize()) {
        androidx.compose.foundation.Image(
          painter = icon ?: painterResource(resourcePath = "ic_launcher.png"),
          contentDescription = iconDescription,
          modifier = iconModifier.size(25.dp).align(Alignment.CenterVertically),
          alignment = iconAlignment,
          contentScale = iconContentScale,
          alpha = iconAlpha,
          colorFilter = iconColorFilter,
        )
        Text(
          text = text!!,
          fontSize = 12.sp,
          maxLines = 1,
          modifier = Modifier.offset(x = 5.dp).align(Alignment.CenterVertically),
        )
      }

      Box(
        modifier = Modifier.fillMaxHeight().width(54.dp).align(Alignment.CenterEnd),
      ) {
        WindowButton(
          onClick = onClose,
          icon = painterResource("close_dark.svg"),
          contentDescription = "Close Dialog",
          onHoverColor = Color(0xFFDB5860),
        )
      }

    } else {
      content()
    }

  }
  Divider(thickness = 0.5.dp)
}
