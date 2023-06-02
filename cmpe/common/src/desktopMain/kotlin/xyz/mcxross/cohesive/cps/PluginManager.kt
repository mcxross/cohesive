package xyz.mcxross.cohesive.cps

import xyz.mcxross.cohesive.common.frontend.api.ui.view.CohesiveView
import okio.Path

/**
 * Provides the functionality for plugin management such as load, start and stop the plugins.
 */
interface PluginManager {
  /** Retrieves all plugins. */
  val plugins: List<PluginWrapper>

  /** Retrieves all plugins with this state. */
  fun pluginsWithState(pluginState: PluginState): List<PluginWrapper>

  /** Retrieves all resolved plugins (with resolved dependency). */
  val resolvedPlugins: List<PluginWrapper>

  /** Retrieves all unresolved plugins (with unresolved dependency). */
  val unresolvedPlugins: List<PluginWrapper>

  /** Retrieves all started plugins. */
  val startedPlugins: List<PluginWrapper>

  /**
   * Retrieves the plugin with this id, or null if the plugin does not exist.
   *
   * @param pluginId the unique plugin identifier, specified in its metadata
   * @return A [PluginWrapper] object for this plugin, or null if it does not exist.
   */
  fun getPlugin(pluginId: String): PluginWrapper

  /** Load plugins. */
  fun loadPlugins()

  /**
   * Load a plugin.
   *
   * @param pluginPath the plugin location
   * @return the pluginId of the installed plugin as specified in its [metadata]
   * [PluginDescriptor]
   * @throws PluginRuntimeException if something goes wrong
   */
  fun loadPlugin(pluginPath: Path): String?

  /** Start all active plugins. */
  fun startPlugins()

  /**
   * Start the specified plugin and its dependencies.
   *
   * @return the plugin state
   * @throws PluginRuntimeException if something goes wrong
   */
  fun startPlugin(pluginId: String): PluginState?

  /** Stop all active plugins. */
  fun stopPlugins()

  /**
   * Stop the specified plugin and its dependencies.
   *
   * @return the plugin state
   * @throws PluginRuntimeException if something goes wrong
   */
  fun stopPlugin(pluginId: String): PluginState?

  /** Unload all plugins */
  fun unloadPlugins()

  /**
   * Unload a plugin.
   *
   * @param pluginId the unique plugin identifier, specified in its metadata
   * @return true if the plugin was unloaded
   * @throws PluginRuntimeException if something goes wrong
   */
  fun unloadPlugin(pluginId: String): Boolean

  /**
   * Disables a plugin from being loaded.
   *
   * @param pluginId the unique plugin identifier, specified in its metadata
   * @return true if plugin is disabled
   * @throws PluginRuntimeException if something goes wrong
   */
  fun disablePlugin(pluginId: String): Boolean

  /**
   * Enables a plugin that has previously been disabled.
   *
   * @param pluginId the unique plugin identifier, specified in its metadata
   * @return true if plugin is enabled
   * @throws PluginRuntimeException if something goes wrong
   */
  fun enablePlugin(pluginId: String): Boolean

  /**
   * Deletes a plugin.
   *
   * @param pluginId the unique plugin identifier, specified in its metadata
   * @return true if the plugin was deleted
   * @throws PluginRuntimeException if something goes wrong
   */
  fun uninstallPlugin(pluginId: String): Boolean
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
   * Set the system version. This is used to compare against the plugin requires attribute. The
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
