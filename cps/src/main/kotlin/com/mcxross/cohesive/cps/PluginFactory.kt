package com.mcxross.cohesive.cps


/**
 * Creates a holder instance.
 */
interface PluginFactory {
    fun create(pluginWrapper: PluginWrapper): Plugin?
}