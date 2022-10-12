package com.mcxross.cohesive.cps


/**
 * Creates a plugin instance.
 */
interface PluginFactory {
    fun create(pluginWrapper: PluginWrapper): Plugin?
}