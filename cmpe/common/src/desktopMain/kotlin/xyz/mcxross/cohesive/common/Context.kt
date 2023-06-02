package xyz.mcxross.cohesive.common

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.window.WindowScope
import xyz.mcxross.cohesive.common.frontend.model.Platform
import xyz.mcxross.cohesive.common.frontend.model.SecondaryPlugin
import xyz.mcxross.cohesive.common.frontend.model.onnet.Descriptor
import xyz.mcxross.cohesive.cps.PluginManager
import xyz.mcxross.cohesive.mellow.PlatformDropTargetModifier

object Local {
  var LocalScreen: ProvidableCompositionLocal<Screen> = staticCompositionLocalOf {
    error("No Screen provided")
  }
}

data class Screen(var scope: WindowScope? = null, var pdtm: PlatformDropTargetModifier)

actual object Context {
  var secondaryPlugins: List<SecondaryPlugin> = emptyList()
  lateinit var platform: Platform
  lateinit var pluginManager: PluginManager
  var isLoadingConfig: Boolean by mutableStateOf(true)
  var isLoadingResource: Boolean by mutableStateOf(true)
  var descriptor: Descriptor by mutableStateOf(Descriptor)
  var configuration: Configuration by mutableStateOf(Configuration)
}
