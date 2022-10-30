package com.mcxross.cohesive.common.frontend.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mcxross.cohesive.common.Context
import com.mcxross.cohesive.common.Local
import com.mcxross.cohesive.common.frontend.model.SecondaryPlugin
import com.mcxross.cohesive.common.frontend.utils.WindowState
import com.mcxross.cohesive.common.frontend.utils.loadImageBitmap
import com.mcxross.cohesive.mellow.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private var selected by mutableStateOf("")

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun Platform(secondaryPlugin: SecondaryPlugin) {
  Column(
    modifier = Modifier.width(120.dp).wrapContentHeight(),
    verticalArrangement = Arrangement.spacedBy(1.dp),
  ) {
    val id by remember { mutableStateOf(secondaryPlugin.id) }
    var progress by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    TooltipArea(
      tooltip = {
        Surface(
          modifier = Modifier.width(250.dp).shadow(4.dp),
          shape = RoundedCornerShape(4.dp),
        ) {
          Text(
            text = secondaryPlugin.description,
            fontSize = 14.sp,
            fontWeight = FontWeight.W300,
            modifier = Modifier.padding(10.dp),
          )
        }
      },
      modifier = Modifier.align(Alignment.CenterHorizontally),
      delayMillis = 800,
      tooltipPlacement =
        TooltipPlacement.CursorPoint(
          alignment = Alignment.BottomEnd,
        ),
    ) {
      Card(
        modifier =
          Modifier.border(
              width = if (selected == id) 1.dp else 0.dp,
              color =
                if (selected == id) MellowTheme.getColors().primary
                else MellowTheme.getColors().surface,
              shape = RoundedCornerShape(15.dp)
            )
            .combinedClickableNoInteractionDesktop(
              interactionSource = interactionSource,
              indication = null,
              onDoubleClick = { progress = !progress },
            ) {
              selected =
                if (selected == id) {
                  ""
                } else {
                  id
                }
            },
        width = 100.dp,
        height = 100.dp,
      ) {
        Image(
          load = {
            loadImageBitmap(
              "https://raw.githubusercontent.com/mcxross/cohesives/main/src/res/" +
                secondaryPlugin.icon.replace(
                  "\"",
                  "",
                ),
            )
          },
          painterFor = { remember { BitmapPainter(it) } },
          contentDescription = secondaryPlugin.name,
          modifier = Modifier.fillMaxSize(),
          contentScale = ContentScale.Inside,
          coroutineDispatcher = Dispatchers.IO,
        )
      }
    }
    AnimatedVisibility(
      visible = progress,
      modifier = Modifier.width(100.dp).align(Alignment.CenterHorizontally),
    ) {
      Row(modifier = Modifier.fillMaxWidth().height(11.dp)) {
        LinearProgressIndicator(
          progress = .2f,
          modifier =
            Modifier.width(90.dp).height(1.dp).align(alignment = Alignment.CenterVertically),
          color = MaterialTheme.colors.primary,
        )
        Icon(
          painter = painterResource("closeSmall_dark.svg"),
          contentDescription = "Cancel Download",
          modifier =
            Modifier.size(10.dp).align(alignment = Alignment.CenterVertically).clickable {
              progress = false
            },
        )
      }
    }
    Text(
      text = secondaryPlugin.name,
      modifier = Modifier.align(Alignment.CenterHorizontally),
      maxLines = 2,
      fontSize = 12.sp,
      fontWeight = FontWeight.W300,
      overflow = TextOverflow.Ellipsis,
    )
  }
}

@Composable
internal fun Platforms(
  modifier: Modifier,
) {
  LazyVerticalGrid(
    columns = GridCells.Adaptive(minSize = 128.dp),
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(26.dp),
    horizontalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    items(Context.secondaryPlugins.size) {
      Platform(secondaryPlugin = Context.secondaryPlugins[it])
    }
  }
}

@Composable
internal fun StoreViewLoading(
  modifier: Modifier = Modifier.padding(top = 30.dp).fillMaxWidth().height(1.dp),
) {
  LinearProgressIndicator(modifier = modifier, color = MellowTheme.getColors().primary)
}

@Composable
internal fun SkipButton(
  modifier: Modifier,
) {
  val scope = rememberCoroutineScope()

  OutlinedButton(
    onClick = {
      WindowState.isPreAvail = true
      scope.launch {
        delay(3000)
        WindowState.isDelayClose = false
      }
    },
    modifier = modifier.offset(x = (-5).dp),
    text = if (selected.isEmpty()) "Skip" else "Proceed",
  )
}

@Composable
fun StoreScreen() {
  DisableSelection {
    MellowTheme.Theme {
      Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
          if (Context.descriptor.isContentReady()) {
            Platforms(
              modifier = Modifier.matchParentSize().align(Alignment.Center).padding(top = 34.dp),
            )
          } else {
            StoreViewLoading()
          }
          Local.LocalScreen.current.scope!!.WindowDraggableArea {
            TopMinBar(
              onClose = { WindowState.isStoreWindowOpen = false },
              text =
                if (selected.isEmpty()) "Select Platform"
                else Context.secondaryPlugins.find { it.id == selected }!!.name + " Selected",
              modifier = Modifier.align(Alignment.TopStart),
            )
          }

          SkipButton(
            modifier = Modifier.align(Alignment.BottomEnd),
          )
        }
      }
    }
  }
}
