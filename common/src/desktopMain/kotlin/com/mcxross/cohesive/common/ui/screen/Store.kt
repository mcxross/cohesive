package com.mcxross.cohesive.common.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.TooltipPlacement
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.WindowScope
import com.mcxross.cohesive.common.model.Plugin
import com.mcxross.cohesive.common.openapi.ui.screen.IStore
import com.mcxross.cohesive.mellow.Image
import com.mcxross.cohesive.mellow.TopMinBar
import com.mcxross.cohesive.mellow.MellowTheme
import com.mcxross.cohesive.common.utils.WindowStateHolder
import com.mcxross.cohesive.common.utils.loadImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.pf4j.Extension

@Extension
open class Store : IStore {

    @Composable
    override fun Emit(windowScope: WindowScope, plugins: List<Plugin>) {

        val scope = rememberCoroutineScope()

        DisableSelection {
            MellowTheme.Theme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        if (plugins.isNotEmpty()) {
                            StoreViewChains(
                                windowScope = windowScope,
                                modifier = Modifier.matchParentSize().align(Alignment.Center)
                                    .padding(start = 40.dp, top = 34.dp),
                                plugins = plugins
                            )
                        } else {
                            StoreViewLoading(
                                windowScope = windowScope,
                            )
                        }
                        windowScope.WindowDraggableArea {
                            TopMinBar(
                                onClose = { WindowStateHolder.isStoreWindowOpen = false },
                                text = "Select Platform",
                                modifier = Modifier.align(Alignment.TopStart)
                            )
                        }
                        com.mcxross.cohesive.mellow.OutlinedButton(
                            onClick = {

                                WindowStateHolder.isPreAvail = true

                                scope.launch {
                                    delay(3000)
                                    WindowStateHolder.isDelayClose = false
                                }

                            },
                            modifier = Modifier.offset(x = (-5).dp).align(Alignment.BottomEnd),
                            text = "Skip"
                        )
                    }

                }
            }
        }

    }


    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    protected fun StoreViewChains(windowScope: WindowScope, modifier: Modifier, plugins: List<Plugin>) {

        LazyVerticalGrid(
            cells = GridCells.Adaptive(minSize = 128.dp),
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(26.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(plugins.size) { plugin ->
                PluginItem(plugin = plugins[plugin])
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
    @Composable
    protected fun PluginItem(plugin: Plugin) {
        var hovered by remember { mutableStateOf(false) }
        TooltipArea(
            tooltip = {
                Surface(
                    modifier = Modifier.width(250.dp).shadow(4.dp),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = plugin.description,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W300,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            },
            delayMillis = 600,
            tooltipPlacement = TooltipPlacement.CursorPoint(
                alignment = Alignment.BottomEnd,
            )
        ) {
            Column(modifier = Modifier.size(120.dp), verticalArrangement = Arrangement.spacedBy(1.dp)) {
                Card(
                    modifier = Modifier.size(100.dp)
                        .onPointerEvent(PointerEventType.Enter) { hovered = true }
                        .onPointerEvent(PointerEventType.Exit) { hovered = false },
                    shape = RoundedCornerShape(15.dp),
                ) {
                    Image(
                        load = {
                            loadImageBitmap(
                                "https://raw.githubusercontent.com/mcxross/cohesives/main/src/res/" + plugin.icon.replace(
                                    "\"",
                                    ""
                                )
                            )
                        },
                        painterFor = { remember { BitmapPainter(it) } },
                        contentDescription = plugin.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Inside,
                        coroutineDispatcher = Dispatchers.IO
                    )

                }
                Text(text = plugin.name, maxLines = 2, fontSize = 12.sp, fontWeight = FontWeight.W300, overflow = TextOverflow.Ellipsis)
            }
        }
    }

    @Composable
    protected fun StoreViewLoading(
        windowScope: WindowScope,
        modifier: Modifier = Modifier.padding(top = 30.dp).fillMaxWidth().height(1.dp)
    ) {
        LinearProgressIndicator(modifier = modifier, color = MellowTheme.getColors().primary)
    }

}

