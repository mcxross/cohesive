package xyz.mcxross.cohesive.cps

import xyz.mcxross.cohesive.common.utils.Log

/**
 * It's an implementation of [PluginStateListener] that writes all events to logger (DEBUG level).
 * This listener is added automatically by [DefaultPluginManager] for `dev` mode.
 */
class LoggingPluginStateListener : PluginStateListener {
  override fun pluginStateChanged(event: PluginStateEvent) {
    Log.d {
      "The state of Plugin ${event.plugin.pluginId} has changed from ${event.oldState} to ${event.pluginState}"
    }
  }
}
