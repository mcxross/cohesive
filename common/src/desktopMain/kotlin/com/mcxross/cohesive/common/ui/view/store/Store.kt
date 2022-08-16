package com.mcxross.cohesive.common.ui.view.store

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.WindowScope
import com.mcxross.cohesive.common.model.Chain
import com.mcxross.cohesive.common.openapi.IStoreView
import com.mcxross.cohesive.common.ui.component.TitleBar
import com.mcxross.cohesive.common.ui.theme.AppTheme
import com.mcxross.cohesive.common.utils.WindowStateHolder
import org.pf4j.Extension

@Extension
open class Store : IStoreView {

    @Composable
    override fun View(windowScope: WindowScope, chains: List<Chain>) {

        DisableSelection {
            MaterialTheme(colors = AppTheme.getColors()) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Row(
                            modifier = Modifier.matchParentSize().align(Alignment.Center)
                                .padding(start = 40.dp, top = 34.dp), horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            chains.forEach { chain ->
                                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                                    Card(modifier = Modifier.size(100.dp), shape = RoundedCornerShape(10.dp)) {
                                        Image(
                                            painter = painterResource("ic_launcher.png"),
                                            contentDescription = chain.name,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                    Text(text = chain.name, maxLines = 2, fontSize = 12.sp)
                                }
                            }
                        }
                        windowScope.WindowDraggableArea {
                            TitleBar("Select Chain", modifier = Modifier.align(Alignment.TopStart)) {
                                WindowStateHolder.isStoreWindowOpen = false
                            }
                        }
                    }
                }
            }
        }

    }

}