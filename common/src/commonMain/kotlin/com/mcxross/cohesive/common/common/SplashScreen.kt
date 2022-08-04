package com.mcxross.cohesive.common.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SplashScreen() {
    DisableSelection {
        MaterialTheme(colors = AppTheme.getColors()) {
            Surface(modifier = Modifier.fillMaxSize()) {

            }
        }
    }

}