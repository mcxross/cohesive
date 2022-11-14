package com.mcxross.cohesive.c

import com.mcxross.cohesive.common.utils.Log
import com.mcxross.cohesive.cps.Plugin
import com.mcxross.cohesive.cps.PluginWrapper

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
