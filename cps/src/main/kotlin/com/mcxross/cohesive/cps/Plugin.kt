package com.mcxross.cohesive.cps

/**
 * This class will be extended by all plugins and
 * serve as the common class between a holder and the application.
 */
class Plugin(wrapper: PluginWrapper?) {

    /**
     * Wrapper of the holder.
     */
    var wrapper: PluginWrapper

    /**
     * Constructor to be used by holder manager for holder instantiation.
     * Your plugins have to provide constructor with this exact signature to
     * be successfully loaded by manager.
     */
    init {
        requireNotNull(wrapper) { "Wrapper cannot be null" }
        this.wrapper = wrapper
    }

    /**
     * This method is called by the application when the holder is started.
     * See [PluginManager.startPlugin].
     */
    fun start() {}

    /**
     * This method is called by the application when the holder is stopped.
     * See [PluginManager.stopPlugin].
     */
    fun stop() {}

    /**
     * This method is called by the application when the holder is deleted.
     * See [PluginManager.deletePlugin].
     */
    fun delete() {}
}