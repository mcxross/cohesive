package com.mcxross.cohesive.common.view

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.mcxross.cohesive.common.openapi.ISplashScreen
import org.pf4j.Extension

@Extension
open class SplashScreen : ISplashScreen {
    override fun show() {
        println("Cohesive Desktop...")
    }

    override fun hide() {
        println("Cohesive Desktop...")
    }

    @Composable
    override fun getSplashScreen() {
        Text("Cohesive Desktop...")
    }


}