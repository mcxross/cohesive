package com.mcxross.cohesive.common.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowScope
import com.mcxross.cohesive.common.App
import com.mcxross.cohesive.common.ui.common.AppTheme

@Composable
fun WindowScope.MainScreen() {
    DisableSelection {
        MaterialTheme(colors = AppTheme.getColors()) {

            Surface(
                modifier = Modifier.fillMaxSize(),
            ) {

                WindowDraggableArea {
                    Box(Modifier.fillMaxWidth().height(30.dp)) {

                    }
                }
                App()
            }

        }
    }

}
