package com.mcxross.cohesive.common.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mcxross.cohesive.common.common.AppTheme
import com.mcxross.cohesive.common.openapi.ISplashScreen
import org.pf4j.Extension

@Extension
class SplashScreen: ISplashScreen {
    override fun show() {
       println("Cohesive Desktop...")
    }

    override fun hide() {
        println("Cohesive Desktop...")
    }

    @Composable
    override fun getSplashScreen() {
        DisableSelection {
            MaterialTheme(colors = AppTheme.getColors()) {
                Surface(modifier = Modifier.fillMaxSize()) {

                }
            }
        }
    }

}