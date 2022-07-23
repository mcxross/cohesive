package com.mcxross.cohesive.common.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowScope

@Composable
fun WindowScope.TitleMenuBar() {
    WindowDraggableArea {
        Column(modifier = Modifier.fillMaxWidth().height(30.dp)) {

            Box(modifier = Modifier.fillMaxSize().padding(start = 10.dp)) {
                Row(modifier = Modifier.size(30.dp, 30.dp)) {
                    Image(
                        painterResource("ic_launcher.png"),
                        "App Icon",
                        modifier = Modifier.size(25.dp, 25.dp).align(Alignment.CenterVertically)
                    )
                }
                ClusterMenu(modifier = Modifier.align(Alignment.Center))
                Box(
                    modifier = Modifier.fillMaxHeight().width(162.dp).align(Alignment.CenterEnd)
                ) {
                    WindowButtons()
                }
            }

        }
    }
}

