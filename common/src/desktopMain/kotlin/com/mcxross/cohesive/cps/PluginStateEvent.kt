package com.mcxross.cohesive.cps

import java.util.*

class PluginStateEvent(
  source: PluginManager,
  val plugin: PluginWrapper,
  val oldState: PluginState
) : EventObject(source) {

  val pluginState: PluginState
    get() = plugin.pluginState

  override fun getSource(): PluginManager {
    return super.getSource() as PluginManager
  }

  override fun toString(): String {
    return "PluginStateEvent [Plugin=" +
      plugin.pluginId +
      ", newState=" +
      pluginState +
      ", oldState=" +
      oldState +
      ']'
  }
}
