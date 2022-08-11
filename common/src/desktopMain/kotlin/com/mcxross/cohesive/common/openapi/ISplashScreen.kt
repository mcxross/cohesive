package com.mcxross.cohesive.common.openapi

import androidx.compose.runtime.Composable
import org.pf4j.ExtensionPoint

interface ISplashScreen: ExtensionPoint {
    fun show()
    fun hide()
    @Composable
    fun getSplashScreen()
}