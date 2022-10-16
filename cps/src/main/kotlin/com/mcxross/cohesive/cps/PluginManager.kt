package com.mcxross.cohesive.cps

import java.nio.file.Path

/**
 * Provides the functionality for holder management such as load,
 * start and stop the plugins.
 */
interface PluginManager {
    /**
     * Retrieves all plugins.
     */
    val plugins: List<PluginWrapper>

    /**
     * Retrieves all plugins with this state.
     */
    fun pluginsWithState(pluginState: PluginState): List<PluginWrapper>?

    /**
     * Retrieves all resolved plugins (with resolved dependency).
     */
    val resolvedPlugins: List<Any?>?

    /**
     * Retrieves all unresolved plugins (with unresolved dependency).
     */
    val unresolvedPlugins: List<Any?>?

    /**
     * Retrieves all started plugins.
     */
    val startedPlugins: List<Any?>?

    /**
     * Retrieves the holder with this id, or null if the holder does not exist.
     *
     * @param pluginId the unique holder identifier, specified in its metadata
     * @return A PluginWrapper object for this holder, or null if it does not exist.
     */
    fun getPlugin(pluginId: String): PluginWrapper

    /**
     * Load plugins.
     */
    fun loadPlugins()

    /**
     * Load a holder.
     *
     * @param pluginPath the holder location
     * @return the pluginId of the installed holder as specified in its [metadata][PluginDescriptor]
     * @throws PluginRuntimeException if something goes wrong
     */
    fun loadPlugin(pluginPath: Path): String?

    /**
     * Start all active plugins.
     */
    fun startPlugins()

    /**
     * Start the specified holder and its dependencies.
     *
     * @return the holder state
     * @throws PluginRuntimeException if something goes wrong
     */
    fun startPlugin(pluginId: String): PluginState?

    /**
     * Stop all active plugins.
     */
    fun stopPlugins()

    /**
     * Stop the specified holder and its dependencies.
     *
     * @return the holder state
     * @throws PluginRuntimeException if something goes wrong
     */
    fun stopPlugin(pluginId: String): PluginState?

    /**
     * Unload all plugins
     */
    fun unloadPlugins()

    /**
     * Unload a holder.
     *
     * @param pluginId the unique holder identifier, specified in its metadata
     * @return true if the holder was unloaded
     * @throws PluginRuntimeException if something goes wrong
     */
    fun unloadPlugin(pluginId: String): Boolean

    /**
     * Disables a holder from being loaded.
     *
     * @param pluginId the unique holder identifier, specified in its metadata
     * @return true if holder is disabled
     * @throws PluginRuntimeException if something goes wrong
     */
    fun disablePlugin(pluginId: String): Boolean

    /**
     * Enables a holder that has previously been disabled.
     *
     * @param pluginId the unique holder identifier, specified in its metadata
     * @return true if holder is enabled
     * @throws PluginRuntimeException if something goes wrong
     */
    fun enablePlugin(pluginId: String): Boolean

    /**
     * Deletes a holder.
     *
     * @param pluginId the unique holder identifier, specified in its metadata
     * @return true if the holder was deleted
     * @throws PluginRuntimeException if something goes wrong
     */
    fun deletePlugin(pluginId: String): Boolean
    fun getPluginClassLoader(pluginId: String): ClassLoader
    fun getExtensionClasses(pluginId: String): List<Class<*>?>?
    fun <T> getExtensionClasses(type: Class<T>): List<Class<out T>?>?
    fun <T> getExtensionClasses(type: Class<T>, pluginId: String): List<Class<out T>?>?
    fun <T> getExtensions(type: Class<T>): List<T>?
    fun <T> getExtensions(type: Class<T>, pluginId: String): List<T>?
    fun <T> getExtensions(pluginId: String): List<T>
    fun getExtensionClassNames(pluginId: String): Set<String?>?
    val extensionFactory: ExtensionFactory?

    /**
     * The runtime mode. Must currently be either DEVELOPMENT or DEPLOYMENT.
     */
    var runtimeMode: RuntimeMode?

    /**
     * Returns `true` if the runtime mode is `RuntimeMode.DEVELOPMENT`.
     */
    val isDevelopment: Boolean
        get() = RuntimeMode.DEVELOPMENT == runtimeMode

    /**
     * Returns `true` if the runtime mode is not `RuntimeMode.DEVELOPMENT`.
     */
    val isNotDevelopment: Boolean
        get() = !isDevelopment

    /**
     * Retrieves the [PluginWrapper] that loaded the given class 'clazz'.
     */
    fun whichPlugin(clazz: Class<*>): PluginWrapper?
    fun addPluginStateListener(listener: PluginStateListener)
    fun removePluginStateListener(listener: PluginStateListener)
    /**
     * Returns the system version.
     *
     * @return the system version
     */
    /**
     * Set the system version.  This is used to compare against the holder
     * requires attribute.  The default system version is 0.0.0 which
     * disables all version checking.
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
    @get:Deprecated(
        """Use {@link #getPluginsRoots()} instead to get all paths where plugins are could be installed.
     
      """
    )
    val pluginsRoot: Path?

    /**
     * Gets the a read-only list of all paths of the folders where plugins are installed.
     *
     * @return Paths of plugins roots
     */
    val pluginsRoots: List<Path?>?
    val versionManager: VersionManager?
}