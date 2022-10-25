package com.mcxross.cohesive.cps

import java.util.*


/**
 * PluginStateListener defines the interface for an object that listens to plugin state changes.
 */
interface PluginStateListener : EventListener {
    /**
     * Invoked when a plugin's state (for example DISABLED, STARTED) is changed.
     */
    fun pluginStateChanged(event: PluginStateEvent)
}