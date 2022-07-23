package com.mcxross.cohesive.common.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.window.WindowScope
import androidx.compose.ui.window.WindowState
import com.mcxross.cohesive.common.ui.TitleMenuBar
import com.mcxross.cohesive.common.ui.common.AppTheme
import com.mcxross.cohesive.common.ui.common.Hr

@Composable
fun WindowScope.MainScreen() {
    DisableSelection {
        MaterialTheme(colors = AppTheme.getColors()) {

            Surface(
                modifier = Modifier.fillMaxSize(),
                contentColor = contentColorFor(MaterialTheme.colors.surface)
            ) {
                Column {
                    TitleMenuBar()

                    Hr()

                    with(LocalDensity.current) {
                        Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)) { }
                    }

                }

            }


        }

    }


}
