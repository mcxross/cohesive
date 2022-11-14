package com.mcxross.cohesive.cps

/**
 * This class will be extended by all plugins and serve as the common class between a Plugin and
 * the application.
 */
abstract class Plugin(wrapper: PluginWrapper?) {

  /** Wrapper of the Plugin. */
  var wrapper: PluginWrapper

  /**
   * Constructor to be used by Plugin manager for Plugin instantiation. Your plugins have to
   * provide constructor with this exact signature to be successfully loaded by manager.
   */
  init {
    requireNotNull(wrapper) { "Wrapper cannot be null" }
    this.wrapper = wrapper
  }

  /**
   * This method is called by the application when the Plugin is started. See
   * [PluginManager.startPlugin].
   */
  abstract fun start()

  /**
   * This method is called by the application when the Plugin is stopped. See
   * [PluginManager.stopPlugin].
   */
  abstract fun stop()

  /**
   * This method is called by the application when the Plugin is deleted. See
   * [PluginManager.uninstallPlugin].
   */
  abstract fun uninstall()
}
