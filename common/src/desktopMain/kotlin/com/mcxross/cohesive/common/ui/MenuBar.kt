package com.mcxross.cohesive.common.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowScope

@Composable
fun WindowScope.MenuBar() {
    WindowDraggableArea {
        Column(modifier = Modifier.fillMaxWidth().height(30.dp)) {

            Box(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier.fillMaxHeight().width(162.dp).align(Alignment.CenterEnd)
                ) {
                    Row(modifier = Modifier.fillMaxSize()) {
                        //Icon()
                        //Icon()
                        //Icon()
                    }
                }
            }

        }
    }
}