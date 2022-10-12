package com.mcxross.cohesive.cps


interface PluginStatusProvider {
    /**
     * Checks if the plugin is disabled or not
     *
     * @param pluginId the unique plugin identifier, specified in its metadata
     * @return if the plugin is disabled or not
     */
    fun isPluginDisabled(pluginId: String): Boolean

    /**
     * Disables a plugin from being loaded.
     *
     * @param pluginId the unique plugin identifier, specified in its metadata
     * @throws PluginRuntimeException if something goes wrong
     */
    fun disablePlugin(pluginId: String)

    /**
     * Enables a plugin that has previously been disabled.
     *
     * @param pluginId the unique plugin identifier, specified in its metadata
     * @throws PluginRuntimeException if something goes wrong
     */
    fun enablePlugin(pluginId: String)
}