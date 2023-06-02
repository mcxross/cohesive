package xyz.mcxross.cohesive.cps

import java.util.*

/**
 * PluginStateListener defines the interface for an object that listens to Plugin state changes.
 */
interface PluginStateListener : EventListener {
  /** Invoked when a Plugin's state (for example DISABLED, STARTED) is changed. */
  fun pluginStateChanged(event: PluginStateEvent)
}
