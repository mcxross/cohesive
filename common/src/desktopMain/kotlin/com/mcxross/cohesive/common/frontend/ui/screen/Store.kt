package com.mcxross.cohesive.common.frontend.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.TooltipPlacement
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mcxross.cohesive.common.frontend.model.Local
import com.mcxross.cohesive.common.frontend.openapi.ui.screen.IStore
import com.mcxross.cohesive.common.frontend.ui.widget.TipScaffold
import com.mcxross.cohesive.common.frontend.ui.widget.TipStyle
import com.mcxross.cohesive.common.frontend.utils.WindowState
import com.mcxross.cohesive.common.frontend.utils.loadImageBitmap
import com.mcxross.cohesive.cps.Extension
import com.mcxross.cohesive.mellow.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Extension
open class Store : IStore {

    @Composable
    override fun Compose() {

        var showAppIntro by remember {
            mutableStateOf(true)
        }

        DisableSelection {
            MellowTheme.Theme {
                Surface(modifier = Modifier.fillMaxSize()) {

                    Box(modifier = Modifier.fillMaxSize()) {
                        if (Local.LocalContext.current.environment!!.plugins.isNotEmpty()) {
                            StoreViewChains(
                                modifier = Modifier.matchParentSize().align(Alignment.Center)
                                    .padding(top = 34.dp),
                                plugins = Local.LocalContext.current.environment!!.plugins
                            )
                        } else {
                            StoreViewLoading()
                        }
                        Local.LocalContext.current.windowScope!!.WindowDraggableArea {
                            TopMinBar(
                                onClose = { WindowState.isStoreWindowOpen = false },
                                text = "Select Platform",
                                modifier = Modifier.align(Alignment.TopStart)
                            )
                        }

                        SkipButton(
                            show = Local.LocalContext.current.environment!!.plugins.isNotEmpty(),
                            modifier = Modifier.align(Alignment.BottomEnd)
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
                modifier = Modifier.align(Alignment.CenterHorizontally),
                delayMillis = 800,
                tooltipPlacement = TooltipPlacement.CursorPoint(
                    alignment = Alignment.BottomEnd,
                )
            ) {
                Card(
                    modifier = Modifier.combinedClickableNoInteractionDesktop(
                        interactionSource = interactionSource,
                        indication = null,
                        onDoubleClick = {
                            downLoadProgress = true
                        }
                    ) {},
                    width = 100.dp,
                    height = 100.dp,
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
            }
            AnimatedVisibility(
                visible = downLoadProgress,
                modifier = Modifier.width(100.dp).align(Alignment.CenterHorizontally)
            ) {
                Row(modifier = Modifier.fillMaxWidth().height(11.dp)) {
                    LinearProgressIndicator(
                        progress = .2f,
                        modifier = Modifier.width(90.dp).height(1.dp).align(alignment = Alignment.CenterVertically),
                        color = MaterialTheme.colors.primary
                    )
                    Icon(
                        painter = painterResource("closeSmall_dark.svg"),
                        contentDescription = "Cancel Download",
                        modifier = Modifier.size(10.dp).align(alignment = Alignment.CenterVertically)
                            .clickable { downLoadProgress = false }
                    )
                }
            }
            Text(
                text = plugin.name,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                maxLines = 2,
                fontSize = 12.sp,
                fontWeight = FontWeight.W300,
                overflow = TextOverflow.Ellipsis
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
        TipScaffold(
            tip = show,
            onTip = {
                //App Intro finished!!
            },
            modifier = modifier
        ) {
            OutlinedButton(
                onClick = {
                    WindowState.isPreAvail = true
                    scope.launch {
                        delay(3000)
                        WindowState.isDelayClose = false
                    }
                },
                modifier = modifier.offset(x = (-5).dp).tipTarget(
                    index = 0,
                    style = TipStyle.Default.copy(
                        backgroundColor = Color(0xFF1C0A00), // specify color of background
                        backgroundAlpha = 0.98f, // specify transparency of background
                        targetCircleColor = Color.White // specify color of target circle
                    ),
                    // specify the content to show to introduce app feature
                    content = {
                        Column {
                            Text(
                                text = "Check emails",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Click here to check/send emails",
                                color = Color.White,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                        }
                    },
                ),
                text = "Skip"
            )
        }
    }

}

