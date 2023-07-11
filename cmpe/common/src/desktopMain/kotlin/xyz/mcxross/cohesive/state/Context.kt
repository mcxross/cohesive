package xyz.mcxross.cohesive.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import xyz.mcxross.cohesive.cps.Descriptor
import xyz.mcxross.cohesive.cps.PluginManager
import xyz.mcxross.cohesive.cps.SecondaryPlugin

actual object Context {
  var secondaryPlugins: List<SecondaryPlugin> = emptyList()
  lateinit var platform: Platform
  lateinit var pluginManager: PluginManager
  var isLoadingConfig: Boolean by mutableStateOf(true)
  var isLoadingResource: Boolean by mutableStateOf(true)
  var descriptor: Descriptor by mutableStateOf(Descriptor)
  var configuration: Configuration by mutableStateOf(Configuration)
}
