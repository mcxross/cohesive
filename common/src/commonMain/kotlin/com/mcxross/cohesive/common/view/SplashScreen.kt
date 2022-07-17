package com.mcxross.cohesive.common.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mcxross.cohesive.common.ui.common.AppTheme

@Composable
fun SplashScreen() {
    DisableSelection {
        MaterialTheme(colors = AppTheme.getColors()) {
            Surface(modifier = Modifier.fillMaxSize()) {

            }
        }
    }

}