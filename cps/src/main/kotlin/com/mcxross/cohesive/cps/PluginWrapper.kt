package com.mcxross.cohesive.cps

import java.nio.file.Path

/**
 * A wrapper over plugin instance.
 */
class PluginWrapper(
    pluginManager: PluginManager,
    descriptor: PluginDescriptor,
    pluginPath: Path,
    pluginClassLoader: ClassLoader,
) {
    private val pluginManager: PluginManager
    private val descriptor: PluginDescriptor

    /**
     * Returns the path of this plugin.
     */
    val pluginPath: Path

    /**
     * Returns the plugin class loader used to load classes and resources
     * for this plug-in. The class loader can be used to directly access
     * plug-in resources and classes.
     */
    val pluginClassLoader: ClassLoader
    private var pluginFactory: PluginFactory? = null
    private var pluginState: PluginState
    private val runtimeMode: RuntimeMode

    /**
     * Returns the exception with which the plugin fails to start.
     * See @{link PluginStatus#FAILED}.
     */
    var failedException: Throwable? = null

    var plugin : Plugin? = null
        get() {
            if (field == null) {
                plugin = pluginFactory!!.create(this)
            }
            return field
        }

    init {
        this.pluginManager = pluginManager
        this.descriptor = descriptor
        this.pluginPath = pluginPath
        this.pluginClassLoader = pluginClassLoader
        pluginState = PluginState.CREATED
        runtimeMode = pluginManager.runtimeMode!!
    }

    /**
     * Returns the plugin manager.
     */
    fun getPluginManager(): PluginManager {
        return pluginManager
    }

    /**
     * Returns the plugin descriptor.
     */
    fun getDescriptor(): PluginDescriptor {
        return descriptor
    }

    fun getPluginState(): PluginState {
        return pluginState
    }

    fun getRuntimeMode(): RuntimeMode {
        return runtimeMode
    }

    /**
     * Shortcut
     */
    val pluginId: String
        get() = getDescriptor().pluginId

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

    fun setPluginState(pluginState: PluginState) {
        this.pluginState = pluginState
    }

    fun setPluginFactory(pluginFactory: PluginFactory?) {
        this.pluginFactory = pluginFactory
    }
}