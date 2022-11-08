package com.mcxross.cohesive.cps

import com.mcxross.cohesive.common.frontend.api.ui.view.CohesiveView
import okio.Path

/**
 * Provides the functionality for corePlugin management such as load, start and stop the plugins.
 */
interface PluginManager {
  /** Retrieves all plugins. */
  val plugins: List<PluginWrapper>

  /** Retrieves all plugins with this state. */
  fun pluginsWithState(pluginState: PluginState): List<PluginWrapper>

  /** Retrieves all resolved plugins (with resolved dependency). */
  val resolvedPlugins: List<Any>

  /** Retrieves all unresolved plugins (with unresolved dependency). */
  val unresolvedPlugins: List<Any>

  /** Retrieves all started plugins. */
  val startedPlugins: List<Any>

  /**
   * Retrieves the corePlugin with this id, or null if the corePlugin does not exist.
   *
   * @param pluginId the unique corePlugin identifier, specified in its metadata
   * @return A PluginWrapper object for this corePlugin, or null if it does not exist.
   */
  fun getPlugin(pluginId: String): PluginWrapper

  /** Load plugins. */
  fun loadPlugins()

  /**
   * Load a corePlugin.
   *
   * @param pluginPath the corePlugin location
   * @return the pluginId of the installed corePlugin as specified in its [metadata]
   * [PluginDescriptor]
   * @throws PluginRuntimeException if something goes wrong
   */
  fun loadPlugin(pluginPath: Path): String?

  /** Start all active plugins. */
  fun startPlugins()

  /**
   * Start the specified corePlugin and its dependencies.
   *
   * @return the corePlugin state
   * @throws PluginRuntimeException if something goes wrong
   */
  fun startPlugin(pluginId: String): PluginState?

  /** Stop all active plugins. */
  fun stopPlugins()

  /**
   * Stop the specified corePlugin and its dependencies.
   *
   * @return the corePlugin state
   * @throws PluginRuntimeException if something goes wrong
   */
  fun stopPlugin(pluginId: String): PluginState?

  /** Unload all plugins */
  fun unloadPlugins()

  /**
   * Unload a corePlugin.
   *
   * @param pluginId the unique corePlugin identifier, specified in its metadata
   * @return true if the corePlugin was unloaded
   * @throws PluginRuntimeException if something goes wrong
   */
  fun unloadPlugin(pluginId: String): Boolean

  /**
   * Disables a corePlugin from being loaded.
   *
   * @param pluginId the unique corePlugin identifier, specified in its metadata
   * @return true if corePlugin is disabled
   * @throws PluginRuntimeException if something goes wrong
   */
  fun disablePlugin(pluginId: String): Boolean

  /**
   * Enables a corePlugin that has previously been disabled.
   *
   * @param pluginId the unique corePlugin identifier, specified in its metadata
   * @return true if corePlugin is enabled
   * @throws PluginRuntimeException if something goes wrong
   */
  fun enablePlugin(pluginId: String): Boolean

  /**
   * Deletes a corePlugin.
   *
   * @param pluginId the unique corePlugin identifier, specified in its metadata
   * @return true if the corePlugin was deleted
   * @throws PluginRuntimeException if something goes wrong
   */
  fun deletePlugin(pluginId: String): Boolean
  fun getCohesiveView(): CohesiveView?
  fun getPluginClassLoader(pluginId: String): ClassLoader
  fun getExtensionClasses(pluginId: String): List<Class<*>?>
  fun <T> getExtensionClasses(type: Class<T>): List<Class<out T>?>
  fun <T> getExtensionClasses(type: Class<T>, pluginId: String): List<Class<out T>?>
  fun <T> getExtensions(type: Class<T>): List<T>
  fun <T> getExtensions(type: Class<T>, pluginId: String): List<T>
  fun <T> getExtensions(pluginId: String): List<T>
  fun getExtensionClassNames(pluginId: String): Set<String?>
  val extensionFactory: ExtensionFactory

  /** The runtime mode. Must currently be either DEVELOPMENT or DEPLOYMENT. */
  var runtimeMode: RuntimeMode?

  /** Returns `true` if the runtime mode is `RuntimeMode.DEVELOPMENT`. */
  val isDevelopment: Boolean
    get() = RuntimeMode.DEVELOPMENT == runtimeMode

  /** Returns `true` if the runtime mode is not `RuntimeMode.DEVELOPMENT`. */
  val isNotDevelopment: Boolean
    get() = !isDevelopment

  /** Retrieves the [PluginWrapper] that loaded the given class 'clazz'. */
  fun whichPlugin(clazz: Class<*>): PluginWrapper?
  fun addPluginStateListener(listener: PluginStateListener)
  fun removePluginStateListener(listener: PluginStateListener)
  /**
   * Returns the system version.
   *
   * @return the system version
   */
  /**
   * Set the system version. This is used to compare against the corePlugin requires attribute. The
   * default system version is 0.0.0 which disables all version checking.
   *
   * @default 0.0.0
   * @param version
   */
  var systemVersion: String

  /**
   * Gets the first path of the folders where plugins are installed.
   *
   * @return Path of plugins root
   */
  val pluginsRoot: Path?

  /**
   * Gets the read-only list of all paths of the folders where plugins are installed.
   *
   * @return Paths of plugins roots
   */
  val pluginsRoots: List<Path?>
  val versionManager: VersionManager
}
