package com.mcxross.cohesive.cps


interface PluginStatusProvider {
    /**
     * Checks if the holder is disabled or not
     *
     * @param pluginId the unique holder identifier, specified in its metadata
     * @return if the holder is disabled or not
     */
    fun isPluginDisabled(pluginId: String): Boolean

    /**
     * Disables a holder from being loaded.
     *
     * @param pluginId the unique holder identifier, specified in its metadata
     * @throws PluginRuntimeException if something goes wrong
     */
    fun disablePlugin(pluginId: String)

    /**
     * Enables a holder that has previously been disabled.
     *
     * @param pluginId the unique holder identifier, specified in its metadata
     * @throws PluginRuntimeException if something goes wrong
     */
    fun enablePlugin(pluginId: String)
}