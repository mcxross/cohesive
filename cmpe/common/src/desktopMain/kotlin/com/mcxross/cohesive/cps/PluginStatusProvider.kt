package com.mcxross.cohesive.cps

interface PluginStatusProvider {
  /**
   * Checks if the Plugin is disabled or not
   *
   * @param pluginId the unique Plugin identifier, specified in its metadata
   * @return if the Plugin is disabled or not
   */
  fun isPluginDisabled(pluginId: String): Boolean

  /**
   * Disables a Plugin from being loaded.
   *
   * @param pluginId the unique Plugin identifier, specified in its metadata
   * @throws PluginRuntimeException if something goes wrong
   */
  fun disablePlugin(pluginId: String)

  /**
   * Enables a Plugin that has previously been disabled.
   *
   * @param pluginId the unique Plugin identifier, specified in its metadata
   * @throws PluginRuntimeException if something goes wrong
   */
  fun enablePlugin(pluginId: String)
}
