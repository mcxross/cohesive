package com.mcxross.cohesive.common.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.window.WindowScope
import com.mcxross.cohesive.common.ui.MenuBar
import com.mcxross.cohesive.common.ui.common.AppTheme
import com.mcxross.cohesive.common.ui.common.Hr

@Composable
fun WindowScope.MainScreen() {
    DisableSelection {
        MaterialTheme(colors = AppTheme.getColors()) {

            Surface(
                modifier = Modifier.fillMaxSize(),
            ) {
                Column {
                    MenuBar()

                    Hr()

                    with(LocalDensity.current) {
                        Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)) { }
                    }

                }

            }


        }

    }


}
