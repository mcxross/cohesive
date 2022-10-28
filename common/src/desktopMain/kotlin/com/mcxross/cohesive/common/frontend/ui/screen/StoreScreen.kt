package com.mcxross.cohesive.common.frontend.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.TooltipPlacement
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.mcxross.cohesive.common.frontend.model.Local
import com.mcxross.cohesive.common.frontend.openapi.ui.screen.IStore
import com.mcxross.cohesive.common.frontend.utils.WindowState
import com.mcxross.cohesive.common.frontend.utils.loadImageBitmap
import com.mcxross.cohesive.csp.annotation.Extension
import com.mcxross.cohesive.mellow.Card
import com.mcxross.cohesive.mellow.Image
import com.mcxross.cohesive.mellow.MellowTheme
import com.mcxross.cohesive.mellow.OutlinedButton
import com.mcxross.cohesive.mellow.TopMinBar
import com.mcxross.cohesive.mellow.combinedClickableNoInteractionDesktop
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Extension
open class StoreScreen : IStore {

  @Composable
  override fun Compose() {

    DisableSelection {
      MellowTheme.Theme {
        Surface(modifier = Modifier.fillMaxSize()) {

          Box(modifier = Modifier.fillMaxSize()) {
            if (Local.LocalContext.current.environment!!.plugins.isNotEmpty()) {
              StoreViewChains(
                modifier = Modifier.matchParentSize().align(Alignment.Center)
                  .padding(top = 34.dp),
                plugins = Local.LocalContext.current.environment!!.plugins,
              )
            } else {
              StoreViewLoading()
            }
            Local.LocalContext.current.windowScope!!.WindowDraggableArea {
              TopMinBar(
                onClose = { WindowState.isStoreWindowOpen = false },
                text = "Select Platform",
                modifier = Modifier.align(Alignment.TopStart),
              )
            }

            SkipButton(
              show = Local.LocalContext.current.environment!!.plugins.isNotEmpty(),
              modifier = Modifier.align(Alignment.BottomEnd),
            )

          }
        }
      }
    }
  }


  @OptIn(ExperimentalFoundationApi::class)
  @Composable
  protected fun StoreViewChains(
    modifier: Modifier,
    plugins: List<com.mcxross.cohesive.common.frontend.model.Plugin>,
  ) {

    LazyVerticalGrid(
      columns = GridCells.Adaptive(minSize = 128.dp),
      modifier = modifier,
      verticalArrangement = Arrangement.spacedBy(26.dp),
      horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
      items(plugins.size) { plugin ->
        PluginItem(plugin = plugins[plugin])
      }
    }
  }

  @OptIn(ExperimentalFoundationApi::class)
  @Composable
  protected fun PluginItem(plugin: com.mcxross.cohesive.common.frontend.model.Plugin) {

    Column(
      modifier = Modifier.width(120.dp).wrapContentHeight(),
      verticalArrangement = Arrangement.spacedBy(1.dp),
    ) {
      var downLoadProgress by remember { mutableStateOf(false) }
      val interactionSource = remember { MutableInteractionSource() }
      TooltipArea(
        tooltip = {
          Surface(
            modifier = Modifier.width(250.dp).shadow(4.dp),
            shape = RoundedCornerShape(4.dp),
          ) {
            Text(
              text = plugin.description,
              fontSize = 14.sp,
              fontWeight = FontWeight.W300,
              modifier = Modifier.padding(10.dp),
            )
          }
        },
        modifier = Modifier.align(Alignment.CenterHorizontally),
        delayMillis = 800,
        tooltipPlacement = TooltipPlacement.CursorPoint(
          alignment = Alignment.BottomEnd,
        ),
      ) {
        Card(
          modifier = Modifier.combinedClickableNoInteractionDesktop(
            interactionSource = interactionSource,
            indication = null,
            onDoubleClick = {
              downLoadProgress = true
            },
          ) {},
          width = 100.dp,
          height = 100.dp,
        ) {
          Image(
            load = {
              loadImageBitmap(
                "https://raw.githubusercontent.com/mcxross/cohesives/main/src/res/" + plugin.icon.replace(
                  "\"",
                  "",
                ),
              )
            },
            painterFor = { remember { BitmapPainter(it) } },
            contentDescription = plugin.name,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Inside,
            coroutineDispatcher = Dispatchers.IO,
          )

        }
      }
      AnimatedVisibility(
        visible = downLoadProgress,
        modifier = Modifier.width(100.dp).align(Alignment.CenterHorizontally),
      ) {
        Row(modifier = Modifier.fillMaxWidth().height(11.dp)) {
          LinearProgressIndicator(
            progress = .2f,
            modifier = Modifier.width(90.dp).height(1.dp)
              .align(alignment = Alignment.CenterVertically),
            color = MaterialTheme.colors.primary,
          )
          Icon(
            painter = painterResource("closeSmall_dark.svg"),
            contentDescription = "Cancel Download",
            modifier = Modifier.size(10.dp).align(alignment = Alignment.CenterVertically)
              .clickable { downLoadProgress = false },
          )
        }
      }
      Text(
        text = plugin.name,
        modifier = Modifier.align(Alignment.CenterHorizontally),
        maxLines = 2,
        fontSize = 12.sp,
        fontWeight = FontWeight.W300,
        overflow = TextOverflow.Ellipsis,
      )

    }
  }

  @Composable
  protected fun StoreViewLoading(
    modifier: Modifier = Modifier.padding(top = 30.dp).fillMaxWidth().height(1.dp),
  ) {
    LinearProgressIndicator(modifier = modifier, color = MellowTheme.getColors().primary)
  }

  @Composable
  private fun SkipButton(
    show: Boolean,
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
      text = "Skip",
    )
  }


}

