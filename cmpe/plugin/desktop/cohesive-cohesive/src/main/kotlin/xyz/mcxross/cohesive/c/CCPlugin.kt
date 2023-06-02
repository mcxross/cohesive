package xyz.mcxross.cohesive.c

import xyz.mcxross.cohesive.common.utils.Log
import xyz.mcxross.cohesive.cps.Plugin
import xyz.mcxross.cohesive.cps.PluginWrapper

class CCPlugin(pluginWrapper : PluginWrapper) : Plugin(pluginWrapper) {
  override fun start() {
    Log.d { "CCPlugin started" }
  }

  override fun stop() {
    Log.d { "CCPlugin stopped" }
  }

  override fun uninstall() {
    Log.d { "CCPlugin uninstalled" }
  }

}
