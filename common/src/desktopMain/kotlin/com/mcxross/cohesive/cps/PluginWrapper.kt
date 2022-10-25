package com.mcxross.cohesive.cps

import okio.Path

/**
 * A wrapper over plugin instance.
 */
class PluginWrapper(
    val pluginManager: PluginManager,
    val descriptor: PluginDescriptor,
    /**
     * Returns the path of this plugin.
     */
    val pluginPath: Path,
    /**
     * Returns the plugin class pluginLoader used to load classes and resources
     * for this plug-in. The class pluginLoader can be used to directly access
     * plug-in resources and classes.
     */
    val pluginClassLoader: ClassLoader,
) {

    var pluginFactory: PluginFactory? = null
    var pluginState: PluginState = PluginState.CREATED
    var runtimeMode: RuntimeMode = pluginManager.runtimeMode!!

    /**
     * Returns the exception with which the plugin fails to start.
     * See @{link PluginStatus#FAILED}.
     */
    var failedException: Throwable? = null

    var plugin: Plugin? = null
        get() {
            if (field == null) {
                plugin = pluginFactory!!.create(this)
            }
            return field
        }

    /**
     * Shortcut
     */
    val pluginId: String
        get() = descriptor.pluginId

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + descriptor.pluginId.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null) {
            return false
        }
        if (javaClass != other.javaClass) {
            return false
        }
        val other1 = other as PluginWrapper
        return descriptor.pluginId == other1.descriptor.pluginId
    }

    override fun toString(): String {
        return "PluginWrapper [descriptor=$descriptor, pluginPath=$pluginPath]"
    }
}