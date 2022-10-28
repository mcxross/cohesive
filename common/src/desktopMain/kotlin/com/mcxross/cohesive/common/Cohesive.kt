package com.mcxross.cohesive.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.application
import com.mcxross.cohesive.common.utils.Log
import com.mcxross.cohesive.cps.pluginManager

object Cohesive {

  private val pluginManager = pluginManager {}

  val LocalPluginManager = compositionLocalOf { pluginManager }

  init {
    //Must initialize logging before anything else
    Log.init()

    Log.i { "Cohesive starting..." }

  }

  fun run(
    content: @Composable (ApplicationScope.() -> Unit),
  ) {
    application {
      CompositionLocalProvider(LocalPluginManager provides pluginManager) {
        content()
      }
    }
  }

}
