package com.mcxross.cohesive.common.ui.view.store

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.WindowScope
import com.mcxross.cohesive.common.model.Plugin
import com.mcxross.cohesive.common.openapi.IStoreViewContainer
import com.mcxross.cohesive.common.ui.component.TitleBar
import com.mcxross.cohesive.common.ui.theme.AppTheme
import com.mcxross.cohesive.common.utils.WindowStateHolder
import org.pf4j.Extension

@Extension
open class StoreViewContainer : IStoreViewContainer {

    @Composable
    override fun StoreView(windowScope: WindowScope, plugins: List<Plugin>) {

        DisableSelection {
            MaterialTheme(colors = AppTheme.getColors()) {
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
                            TitleBar("Select Chain Plugin", modifier = Modifier.align(Alignment.TopStart)) {
                                WindowStateHolder.isStoreWindowOpen = false
                            }
                        }
                    }

                }
            }
        }

    }

    @Composable
    protected fun StoreViewChains(windowScope: WindowScope, modifier: Modifier, plugins: List<Plugin>) {
        Row(
            modifier = modifier, horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            plugins.forEach { chain ->
                PluginItem(plugin = chain)
            }
        }
    }

    @Composable
    protected fun PluginItem(plugin: Plugin) {

        Column(modifier = Modifier.size(150.dp), verticalArrangement = Arrangement.spacedBy(1.dp)) {
            Card(modifier = Modifier.size(100.dp), shape = RoundedCornerShape(10.dp)) {
                Image(
                    painter = painterResource("ic_launcher.png"),
                    contentDescription = plugin.name,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Text(text = plugin.name, maxLines = 2, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(3.dp))
            Text(text = plugin.description, maxLines = 1, fontSize = 10.sp, overflow = TextOverflow.Ellipsis)
        }

    }

    @Composable
    protected fun StoreViewLoading(windowScope: WindowScope, modifier: Modifier = Modifier.padding(top = 30.dp).fillMaxWidth().height(1.dp)) {
        LinearProgressIndicator(modifier = modifier, color = AppTheme.getColors().primary)
    }

}

