package com.mcxross.cohesive.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.application
import com.mcxross.cohesive.common.utils.Log
import com.mcxross.cohesive.cps.DefaultPluginManager

object Cohesive {

    init {
        //Must initialize logging before anything else
        Log.init()

        Log.i { "Cohesive starting..." }

        val pluginManager = DefaultPluginManager()
    }

    fun run(
        content: @Composable (ApplicationScope.() -> Unit),
    ) {
        application {
            content()
        }
    }

}
