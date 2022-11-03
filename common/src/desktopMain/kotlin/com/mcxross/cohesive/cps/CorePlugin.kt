package com.mcxross.cohesive.cps

/**
 * This class will be extended by all plugins and
 * serve as the common class between a corePlugin and the application.
 */
abstract class CorePlugin(wrapper: PluginWrapper?) {

  /**
   * Wrapper of the corePlugin.
   */
  var wrapper: PluginWrapper

  /**
   * Constructor to be used by corePlugin manager for corePlugin instantiation.
   * Your plugins have to provide constructor with this exact signature to
   * be successfully loaded by manager.
   */
  init {
    requireNotNull(wrapper) { "Wrapper cannot be null" }
    this.wrapper = wrapper
  }

  /**
   * This method is called by the application when the corePlugin is started.
   * See [PluginManager.startPlugin].
   */
  abstract fun start()

  /**
   * This method is called by the application when the corePlugin is stopped.
   * See [PluginManager.stopPlugin].
   */
  abstract fun stop()

  /**
   * This method is called by the application when the corePlugin is deleted.
   * See [PluginManager.deletePlugin].
   */
  abstract fun delete()
}
