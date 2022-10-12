package com.mcxross.cohesive.cps

import com.mcxross.cohesive.cps.utils.Log
import com.mcxross.cohesive.cps.utils.StringUtils
import java.io.Closeable
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import java.util.function.Consumer
import java.util.stream.Collectors


/**
 * This class implements the boilerplate plugin code that any [PluginManager]
 * implementation would have to support.
 * It helps cut the noise out of the subclass that handles plugin management.
 */
abstract class AbstractPluginManager : PluginManager {


    override val pluginsRoots: MutableList<Path> = ArrayList()
        get() {
            return Collections.unmodifiableList(field)
        }
    protected var extensionFinder: ExtensionFinder? = null
    protected var pluginDescriptorFinder: PluginDescriptorFinder? = null

    /**
     * A map of plugins this manager is responsible for (the key is the 'pluginId').
     */
    protected var pluginsMap: MutableMap<String?, PluginWrapper> = mutableMapOf()


    /**
     * Retrieve all plugins.
     * */
    override val plugins: List<PluginWrapper>
        get() = ArrayList(pluginsMap.values)

    /**
     * A map of plugin class loaders (the key is the 'pluginId').
     */
    protected var pluginClassLoaders: MutableMap<String?, ClassLoader> = mutableMapOf()

    /**
     * A list with unresolved plugins (unresolved dependency).
     */
    override var unresolvedPlugins: MutableList<PluginWrapper> = mutableListOf()

    /**
     * A list with all resolved plugins (resolved dependency).
     */
    override var resolvedPlugins: MutableList<PluginWrapper> = mutableListOf()

    /**
     * A list with started plugins.
     */
    override var startedPlugins: MutableList<PluginWrapper> = mutableListOf()

    /**
     * The registered [PluginStateListener]s.
     */
    protected var pluginStateListeners: MutableList<PluginStateListener> = mutableListOf()

    /**
     * Cache value for the runtime mode.
     * No need to re-read it because it wont change at runtime.
     */
    override var runtimeMode: RuntimeMode? = null
        get() {
            if (field == null) {
                // retrieves the runtime mode from system
                val modeAsString: String = System.getProperty(MODE_PROPERTY_NAME, RuntimeMode.DEPLOYMENT.toString())
                runtimeMode = RuntimeMode.byName(modeAsString)
            }
            return field
        }

    /**
     * The system version used for comparisons to the plugin requires attribute.
     */
    override var systemVersion: String = "0.0.0"
    protected var pluginRepository: PluginRepository? = null
    protected var pluginFactory: PluginFactory? = null
    override var extensionFactory: ExtensionFactory? = null
    protected var pluginStatusProvider: PluginStatusProvider? = null
    protected var dependencyResolver: DependencyResolver? = null
    protected var pluginLoader: PluginLoader? = null

    /**
     * Set to true to allow requires expression to be exactly x.y.z.
     * The default is false, meaning that using an exact version x.y.z will
     * implicitly mean the same as >=x.y.z
     *
     * @param exactVersionAllowed set to true or false
     */
    var isExactVersionAllowed: Boolean = false
    override var versionManager: VersionManager? = null
    override val pluginsRoot: Path
        get() = pluginsRoots.stream()
            .findFirst()
            .orElseThrow {
                IllegalStateException(
                    "pluginsRoots have not been initialized, yet."
                )
            }
    val version: String
        get() {
            return "Cohesive.VERSION"
        }

    /**
     * The plugins roots are supplied as comma-separated list by `System.getProperty("pf4j.pluginsDir", "plugins")`.
     */
    constructor() {
        initialize()
    }

    /**
     * Constructs `AbstractPluginManager` with the given plugins roots.
     *
     * @param pluginsRoots the roots to search for plugins
     */
    constructor(vararg pluginsRoots: Path) : this(listOf<Path>(*pluginsRoots))

    /**
     * Constructs `AbstractPluginManager` with the given plugins roots.
     *
     * @param pluginsRoots the roots to search for plugins
     */
    constructor(pluginsRoots: List<Path>) {
        this.pluginsRoots.plus((pluginsRoots))
        initialize()
    }

    /**
     * Returns a copy of plugins with that state.
     */
    override fun pluginsWithState(pluginState: PluginState): List<PluginWrapper>? {

        val plugins: MutableList<PluginWrapper> = ArrayList<PluginWrapper>()
        for (plugin: PluginWrapper in plugins) {
            if ((pluginState == plugin.getPluginState())) {
                plugins.add(plugin)
            }
        }
        return plugins

    }

    override fun getPlugin(pluginId: String): PluginWrapper {
        return pluginsMap[pluginId]!!
    }

    override fun loadPlugin(pluginPath: Path): String {
        if (Files.notExists(pluginPath)) {
            throw IllegalArgumentException(String.format("Specified plugin %s does not exist!", pluginPath))
        }
        Log.d { "Loading plugin from $pluginPath" }
        val pluginWrapper: PluginWrapper? = loadPluginFromPath(pluginPath)

        // try to resolve  the loaded plugin together with other possible plugins that depend on this plugin
        resolvePlugins()
        return pluginWrapper?.getDescriptor()!!.pluginId
    }

    /**
     * Load plugins.
     */
    override fun loadPlugins() {
        Log.d { "Lookup plugins in $pluginsRoots" }
        // check for plugins roots
        if (pluginsRoots.isEmpty()) {
            Log.w { "No plugins roots configured" }
            return
        }
        pluginsRoots.forEach(Consumer { path: Path ->
            if (Files.notExists(path) || !Files.isDirectory(path)) {
                Log.w { "No $path root" }
            }
        })

        // get all plugin paths from repository
        val pluginPaths: List<Path> = pluginRepository!!.pluginPaths

        // check for no plugins
        if (pluginPaths.isEmpty()) {
            Log.i { "No plugins" }
            return
        }
        Log.d { "Found ${pluginPaths.size} possible plugins: $pluginPaths" }

        // load plugins from plugin paths
        for (pluginPath: Path? in pluginPaths) {
            try {
                pluginPath?.let { loadPluginFromPath(it) }
            } catch (e: PluginRuntimeException) {
                Log.e { e.message.toString() }
            }
        }

        // resolve plugins
        try {
            resolvePlugins()
        } catch (e: PluginRuntimeException) {
            Log.e { e.message.toString() }
        }
    }

    /**
     * Unload all plugins
     */
    override fun unloadPlugins() {
        // wrap resolvedPlugins in new list because of concurrent modification
        for (pluginWrapper: PluginWrapper in ArrayList(resolvedPlugins)) {
            unloadPlugin(pluginWrapper.pluginId)
        }
    }

    /**
     * Unload the specified plugin and it's dependents.
     */
    override fun unloadPlugin(pluginId: String): Boolean {
        return unloadPlugin(pluginId, true)
    }

    protected fun unloadPlugin(pluginId: String, unloadDependents: Boolean): Boolean {
        try {
            if (unloadDependents) {
                val dependents: MutableList<String> = dependencyResolver!!.getDependents(pluginId)
                while (dependents.isNotEmpty()) {
                    val dependent: String = dependents.removeAt(0)
                    unloadPlugin(dependent, false)
                    dependents.addAll(0, dependencyResolver!!.getDependents(dependent))
                }
            }
            val pluginState: PluginState = stopPlugin(pluginId, false)
            if (PluginState.STARTED == pluginState) {
                return false
            }
            val pluginWrapper: PluginWrapper = getPlugin(pluginId)
            Log.i { "Unload plugin ${getPluginLabel(pluginWrapper.getDescriptor())}" }

            // remove the plugin
            pluginsMap!!.remove(pluginId)
            resolvedPlugins.remove(pluginWrapper)
            firePluginStateEvent(PluginStateEvent(this, pluginWrapper, pluginState))

            // remove the classloader
            val pluginClassLoaders: MutableMap<String?, ClassLoader> =
                pluginClassLoaders
            if (pluginClassLoaders.containsKey(pluginId)) {
                val classLoader: ClassLoader? = pluginClassLoaders.remove(pluginId)
                if (classLoader is Closeable) {
                    try {
                        (classLoader as Closeable).close()
                    } catch (e: IOException) {
                        throw PluginRuntimeException(e, "Cannot close classloader")
                    }
                }
            }
            return true
        } catch (e: IllegalArgumentException) {
            // ignore not found exceptions because this method is recursive
        }
        return false
    }

    override fun deletePlugin(pluginId: String): Boolean {
        checkPluginId(pluginId)
        val pluginWrapper: PluginWrapper = getPlugin(pluginId)
        // stop the plugin if it's started
        val pluginState: PluginState = stopPlugin(pluginId)
        if (PluginState.STARTED == pluginState) {
            Log.e { "Failed to stop plugin $pluginId on delete" }
            return false
        }

        // get an instance of plugin before the plugin is unloaded
        // for reason see https://github.com/pf4j/pf4j/issues/309
        val plugin: Plugin? = pluginWrapper.plugin
        if (!unloadPlugin(pluginId)) {
            Log.e { "Failed to unload plugin $pluginId on delete" }
            return false
        }

        // notify the plugin as it's deleted
        plugin!!.delete()
        val pluginPath: Path = pluginWrapper.pluginPath
        return pluginRepository!!.deletePluginPath(pluginPath)
    }

    /**
     * Start all active plugins.
     */
    override fun startPlugins() {
        for (pluginWrapper: PluginWrapper in resolvedPlugins) {
            val pluginState: PluginState = pluginWrapper.getPluginState()
            if ((PluginState.DISABLED != pluginState) && (PluginState.STARTED != pluginState)) {
                try {
                    Log.i { "Start plugin ${getPluginLabel(pluginWrapper.getDescriptor())}" }
                    pluginWrapper.plugin!!.start()
                    pluginWrapper.setPluginState(PluginState.STARTED)
                    pluginWrapper.failedException = null
                    startedPlugins.add(pluginWrapper)
                } catch (e: Exception) {
                    pluginWrapper.setPluginState(PluginState.FAILED)
                    pluginWrapper.failedException = e
                    Log.e { "Unable to start plugin ${getPluginLabel(pluginWrapper.getDescriptor())}" }
                } catch (e: LinkageError) {
                    pluginWrapper.setPluginState(PluginState.FAILED)
                    pluginWrapper.failedException = e
                    Log.e { "Unable to start plugin ${getPluginLabel(pluginWrapper.getDescriptor())}" }
                } finally {
                    firePluginStateEvent(PluginStateEvent(this, pluginWrapper, pluginState))
                }
            }
        }
    }

    /**
     * Start the specified plugin and its dependencies.
     */
    override fun startPlugin(pluginId: String): PluginState {
        checkPluginId(pluginId)
        val pluginWrapper: PluginWrapper = getPlugin(pluginId)
        val pluginDescriptor: PluginDescriptor = pluginWrapper.getDescriptor()
        val pluginState: PluginState = pluginWrapper.getPluginState()
        if (PluginState.STARTED == pluginState) {
            Log.d { "Already started plugin ${getPluginLabel(pluginDescriptor)}" }
            return PluginState.STARTED
        }
        if (!resolvedPlugins.contains(pluginWrapper)) {
            Log.w { "Cannot start an unresolved plugin ${getPluginLabel(pluginDescriptor)}" }
            return pluginState
        }
        if (PluginState.DISABLED == pluginState) {
            // automatically enable plugin on manual plugin start
            if (!enablePlugin(pluginId)) {
                return pluginState
            }
        }
        for (dependency: PluginDependency in pluginDescriptor.dependencies!!) {
            // start dependency only if it marked as required (non optional) or if it optional and loaded
            if (!dependency.isOptional || pluginsMap.containsKey(dependency.pluginId)) {
                dependency.pluginId?.let { startPlugin(it) }
            }
        }
        Log.i { "Start plugin ${getPluginLabel(pluginDescriptor)}" }
        pluginWrapper.plugin!!.start()
        pluginWrapper.setPluginState(PluginState.STARTED)
        startedPlugins.add(pluginWrapper)
        firePluginStateEvent(PluginStateEvent(this, pluginWrapper, pluginState))
        return pluginWrapper.getPluginState()
    }

    /**
     * Stop all active plugins.
     */
    override fun stopPlugins() {
        // stop started plugins in reverse order
        startedPlugins.reversed()

        val itr: MutableIterator<PluginWrapper> = startedPlugins.iterator()
        while (itr.hasNext()) {
            val pluginWrapper: PluginWrapper = itr.next()
            val pluginState: PluginState = pluginWrapper.getPluginState()
            if (PluginState.STARTED == pluginState) {
                try {
                    Log.i { "Stop plugin ${getPluginLabel(pluginWrapper.getDescriptor())}" }
                    pluginWrapper.plugin?.stop()
                    pluginWrapper.setPluginState(PluginState.STOPPED)
                    itr.remove()
                    firePluginStateEvent(PluginStateEvent(this, pluginWrapper, pluginState))
                } catch (e: PluginRuntimeException) {
                    Log.e { e.message.toString() }
                }
            }
        }
    }

    /**
     * Stop the specified plugin and it's dependents.
     */
    override fun stopPlugin(pluginId: String): PluginState {
        return stopPlugin(pluginId, true)
    }

    protected fun stopPlugin(pluginId: String, stopDependents: Boolean): PluginState {
        checkPluginId(pluginId)
        val pluginWrapper: PluginWrapper = getPlugin(pluginId)
        val pluginDescriptor: PluginDescriptor = pluginWrapper.getDescriptor()
        val pluginState: PluginState = pluginWrapper.getPluginState()
        if (PluginState.STOPPED == pluginState) {
            Log.d { "Already stopped plugin ${getPluginLabel(pluginDescriptor)}" }
            return PluginState.STOPPED
        }

        // test for disabled plugin
        if (PluginState.DISABLED == pluginState) {
            // do nothing
            return pluginState
        }
        if (stopDependents) {
            val dependents: MutableList<String> = dependencyResolver!!.getDependents(pluginId)
            while (dependents.isNotEmpty()) {
                val dependent: String = dependents.removeAt(0)
                stopPlugin(dependent, false)
                dependents.addAll(0, dependencyResolver!!.getDependents(dependent))
            }
        }
        Log.i { "Stop plugin ${getPluginLabel(pluginDescriptor)}" }
        pluginWrapper.plugin?.stop()
        pluginWrapper.setPluginState(PluginState.STOPPED)
        startedPlugins.remove(pluginWrapper)
        firePluginStateEvent(PluginStateEvent(this, pluginWrapper, pluginState))
        return pluginWrapper.getPluginState()
    }

    protected fun checkPluginId(pluginId: String?) {
        if (!pluginsMap.containsKey(pluginId)) {
            throw IllegalArgumentException(String.format("Unknown pluginId %s", pluginId))
        }
    }

    override fun disablePlugin(pluginId: String): Boolean {
        checkPluginId(pluginId)
        val pluginWrapper: PluginWrapper = getPlugin(pluginId)
        val pluginDescriptor: PluginDescriptor = pluginWrapper.getDescriptor()
        val pluginState: PluginState = pluginWrapper.getPluginState()
        if (PluginState.DISABLED == pluginState) {
            Log.d { "Already disabled plugin ${getPluginLabel(pluginDescriptor)}" }
            return true
        }
        if (PluginState.STOPPED == stopPlugin(pluginId)) {
            pluginWrapper.setPluginState(PluginState.DISABLED)
            firePluginStateEvent(PluginStateEvent(this, pluginWrapper, PluginState.STOPPED))
            pluginStatusProvider?.disablePlugin(pluginId)
            Log.i { "Disabled plugin ${getPluginLabel(pluginDescriptor)}" }
            return true
        }
        return false
    }

    override fun enablePlugin(pluginId: String): Boolean {
        checkPluginId(pluginId)
        val pluginWrapper: PluginWrapper = getPlugin(pluginId)
        if (!isPluginValid(pluginWrapper)) {
            Log.w { "Plugin ${getPluginLabel(pluginWrapper.getDescriptor())} can not be enabled" }
            return false
        }
        val pluginDescriptor: PluginDescriptor = pluginWrapper.getDescriptor()
        val pluginState: PluginState = pluginWrapper.getPluginState()
        if (PluginState.DISABLED != pluginState) {
            Log.d { "Plugin ${getPluginLabel(pluginDescriptor)} is not disabled" }
            return true
        }
        pluginStatusProvider?.enablePlugin(pluginId)
        pluginWrapper.setPluginState(PluginState.CREATED)
        firePluginStateEvent(PluginStateEvent(this, pluginWrapper, pluginState))
        Log.i { "Enabled plugin ${getPluginLabel(pluginDescriptor)}" }
        return true
    }

    /**
     * Get the [ClassLoader] for plugin.
     */
    override fun getPluginClassLoader(pluginId: String): ClassLoader {
        return (pluginClassLoaders[pluginId])!!
    }

    override fun getExtensionClasses(pluginId: String): List<Class<*>> {
        val extensionsWrapper: List<ExtensionWrapper<Any>?> = extensionFinder!!.find(pluginId)
        val extensionClasses: MutableList<Class<*>> = ArrayList(extensionsWrapper.size)
        for (extensionWrapper: ExtensionWrapper<Any>? in extensionsWrapper) {
            val c: Class<*> = extensionWrapper!!.descriptor.extensionClass
            extensionClasses.add(c)
        }
        return extensionClasses
    }

    override fun <T> getExtensionClasses(type: Class<T>): List<Class<out T>> {
        return getExtensionClasses(extensionFinder!!.find(type))
    }

    override fun <T> getExtensionClasses(type: Class<T>, pluginId: String): List<Class<out T>> {
        return getExtensionClasses(extensionFinder!!.find(type, pluginId))
    }

    override fun <T> getExtensions(type: Class<T>): List<T> {
        return getExtensions(extensionFinder!!.find(type))
    }

    override fun <T> getExtensions(type: Class<T>, pluginId: String): List<T> {
        return getExtensions(extensionFinder!!.find(type, pluginId))
    }

    override fun <T> getExtensions(pluginId: String): List<T> {
        val extensionsWrapper: List<ExtensionWrapper<T>> = extensionFinder!!.find(pluginId)
        val extensions: MutableList<T> = ArrayList<T>(extensionsWrapper.size)
        for (extensionWrapper: ExtensionWrapper<T> in extensionsWrapper) {
            try {
                extensions.add(extensionWrapper.extension!!)
            } catch (e: PluginRuntimeException) {
                Log.e { "Cannot retrieve extension" }
            }
        }
        return extensions
    }

    override fun getExtensionClassNames(pluginId: String): Set<String> {
        return extensionFinder!!.findClassNames(pluginId)
    }

    override fun whichPlugin(clazz: Class<*>): PluginWrapper? {
        val classLoader: ClassLoader = clazz.classLoader
        for (plugin: PluginWrapper in resolvedPlugins) {
            if (plugin.pluginClassLoader === classLoader) {
                return plugin
            }
        }
        return null
    }

    @Synchronized
    override fun addPluginStateListener(listener: PluginStateListener) {
        pluginStateListeners.add(listener)
    }

    @Synchronized
    override fun removePluginStateListener(listener: PluginStateListener) {
        pluginStateListeners.remove(listener)
    }

    protected abstract fun createPluginRepository(): PluginRepository?
    protected abstract fun createPluginFactory(): PluginFactory?
    protected abstract fun createExtensionFactory(): ExtensionFactory?
    protected abstract fun createPluginDescriptorFinder(): PluginDescriptorFinder?
    protected abstract fun createExtensionFinder(): ExtensionFinder?
    protected abstract fun createPluginStatusProvider(): PluginStatusProvider?
    protected abstract fun createPluginLoader(): PluginLoader?
    protected abstract fun createVersionManager(): VersionManager?
    protected open fun initialize() {
        pluginsMap = HashMap<String?, PluginWrapper>()
        pluginClassLoaders = HashMap()
        unresolvedPlugins = ArrayList<PluginWrapper>()
        resolvedPlugins = ArrayList<PluginWrapper>()
        startedPlugins = ArrayList<PluginWrapper>()
        pluginStateListeners = ArrayList<PluginStateListener>()
        if (pluginsRoots.isEmpty()) {
            pluginsRoots.addAll(createPluginsRoot())
        }
        pluginRepository = createPluginRepository()
        pluginFactory = createPluginFactory()
        extensionFactory = createExtensionFactory()
        pluginDescriptorFinder = createPluginDescriptorFinder()
        extensionFinder = createExtensionFinder()
        pluginStatusProvider = createPluginStatusProvider()
        pluginLoader = createPluginLoader()
        versionManager = createVersionManager()
        dependencyResolver = versionManager?.let { DependencyResolver(it) }
    }

    /**
     * Add the possibility to override the plugins roots.
     * If a [.PLUGINS_DIR_PROPERTY_NAME] system property is defined than this method returns that roots.
     * If [.getRuntimeMode] returns [RuntimeMode.DEVELOPMENT] than [.DEVELOPMENT_PLUGINS_DIR]
     * is returned else this method returns [.DEFAULT_PLUGINS_DIR].
     *
     * @return the plugins root
     */
    protected fun createPluginsRoot(): List<Path> {
        var pluginsDir: String? = System.getProperty(PLUGINS_DIR_PROPERTY_NAME)
        if (!pluginsDir.isNullOrEmpty()) {
            return Arrays.stream(pluginsDir.split(",".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray())
                .map { obj: String -> obj.trim { it <= ' ' } }
                .map { first: String ->
                    Paths.get(
                        first
                    )
                }
                .collect(Collectors.toList())
        }
        pluginsDir = if (isDevelopment) DEVELOPMENT_PLUGINS_DIR else DEFAULT_PLUGINS_DIR
        return listOf(Paths.get(pluginsDir))
    }

    /**
     * Check if this plugin is valid (satisfies "requires" param) for a given system version.
     *
     * @param pluginWrapper the plugin to check
     * @return true if plugin satisfies the "requires" or if requires was left blank
     */
    protected fun isPluginValid(pluginWrapper: PluginWrapper): Boolean {
        var requires: String = pluginWrapper.getDescriptor().requires!!.trim { it <= ' ' }
        if (!isExactVersionAllowed && requires.matches(Regex("^\\d+\\.\\d+\\.\\d+$"))) {
            // If exact versions are not allowed in requires, rewrite to >= expression
            requires = ">=$requires"
        }
        if ((systemVersion == "0.0.0") || versionManager!!.checkVersionConstraint(systemVersion, requires)) {
            return true
        }
        val pluginDescriptor: PluginDescriptor = pluginWrapper.getDescriptor()
        Log.w { "Plugin ${getPluginLabel(pluginDescriptor)} requires a minimum system version of $requires, and you have $systemVersion" }
        return false
    }

    protected fun isPluginDisabled(pluginId: String): Boolean {
        return pluginStatusProvider!!.isPluginDisabled(pluginId)
    }

    protected fun resolvePlugins() {
        // retrieves the plugins descriptors
        val descriptors: MutableList<PluginDescriptor> = ArrayList<PluginDescriptor>()
        for (plugin: PluginWrapper in pluginsMap.values) {
            descriptors.add(plugin.getDescriptor())
        }
        val result: DependencyResolver.Result = dependencyResolver!!.resolve(descriptors)
        if (result.hasCyclicDependency()) {
            throw DependencyResolver.CyclicDependencyException()
        }
        val notFoundDependencies: List<String> = result.getNotFoundDependencies()
        if (notFoundDependencies.isNotEmpty()) {
            throw DependencyResolver.DependenciesNotFoundException(notFoundDependencies)
        }
        val wrongVersionDependencies: List<DependencyResolver.WrongDependencyVersion> =
            result.getWrongVersionDependencies()
        if (wrongVersionDependencies.isNotEmpty()) {
            throw DependencyResolver.DependenciesWrongVersionException(wrongVersionDependencies)
        }
        val sortedPlugins: List<String> = result.sortedPlugins

        // move plugins from "unresolved" to "resolved"
        for (pluginId: String? in sortedPlugins) {
            val pluginWrapper: PluginWrapper = pluginsMap[pluginId]!!
            if (unresolvedPlugins.remove(pluginWrapper)) {
                val pluginState: PluginState = pluginWrapper.getPluginState()
                if (pluginState != PluginState.DISABLED) {
                    pluginWrapper.setPluginState(PluginState.RESOLVED)
                }
                resolvedPlugins.add(pluginWrapper)
                Log.i { "Plugin ${getPluginLabel(pluginWrapper.getDescriptor())} resolved" }
                firePluginStateEvent(PluginStateEvent(this, pluginWrapper, pluginState))
            }
        }
    }

    @Synchronized
    protected fun firePluginStateEvent(event: PluginStateEvent) {
        for (listener: PluginStateListener in pluginStateListeners) {
            Log.wtf { "Fire $event to $listener" }
            listener.pluginStateChanged(event)
        }
    }

    protected open fun loadPluginFromPath(pluginPath: Path): PluginWrapper? {
        // Test for plugin path duplication
        var pluginId: String? = idForPath(pluginPath)
        if (pluginId != null) {
            throw PluginAlreadyLoadedException(pluginId, pluginPath)
        }

        // Retrieve and validate the plugin descriptor
        val pluginDescriptorFinder: PluginDescriptorFinder = pluginDescriptorFinder!!
        Log.d { "Use $pluginDescriptorFinder to find plugins descriptors" }
        Log.d { "Finding plugin descriptor for plugin $pluginPath" }
        val pluginDescriptor: PluginDescriptor = pluginDescriptorFinder.find(pluginPath)!!
        validatePluginDescriptor(pluginDescriptor)

        // Check there are no loaded plugins with the retrieved id
        pluginId = pluginDescriptor.pluginId
        if (pluginsMap.containsKey(pluginId)) {
            val loadedPlugin: PluginWrapper = getPlugin(pluginId)
            throw PluginRuntimeException(
                ("There is an already loaded plugin ({}) "
                        + "with the same id ({}) as the plugin at path '{}'. Simultaneous loading "
                        + "of plugins with the same PluginId is not currently supported.\n"
                        + "As a workaround you may include PluginVersion and PluginProvider "
                        + "in PluginId."),
                loadedPlugin, pluginId, pluginPath
            )
        }
        Log.d { "Found descriptor $pluginDescriptor" }
        val pluginClassName: String? = pluginDescriptor.pluginClass
        Log.d { "Class $pluginClassName for plugin $pluginPath" }

        // load plugin
        Log.d { "Loading plugin $pluginPath" }
        val pluginClassLoader: ClassLoader = pluginLoader?.loadPlugin(pluginPath, pluginDescriptor)!!
        Log.d { "Loaded plugin $pluginPath with class loader $pluginClassLoader" }
        val pluginWrapper: PluginWrapper = createPluginWrapper(pluginDescriptor, pluginPath, pluginClassLoader)

        // test for disabled plugin
        if (isPluginDisabled(pluginDescriptor.pluginId)) {
            Log.i { "Plugin $pluginPath is disabled" }
            pluginWrapper.setPluginState(PluginState.DISABLED)
        }

        // validate the plugin
        if (!isPluginValid(pluginWrapper)) {
            Log.w { "Plugin $pluginPath is invalid and it will be disabled" }
            pluginWrapper.setPluginState(PluginState.DISABLED)
        }
        Log.d { "Created wrapper $pluginWrapper for plugin $pluginPath" }
        pluginId = pluginDescriptor.pluginId

        // add plugin to the list with plugins
        pluginsMap[pluginId] = pluginWrapper
        unresolvedPlugins.add(pluginWrapper)

        // add plugin class loader to the list with class loaders
        pluginClassLoaders[pluginId] = pluginClassLoader
        return pluginWrapper
    }

    /**
     * creates the plugin wrapper. override this if you want to prevent plugins having full access to the plugin manager
     *
     * @return
     */
    protected fun createPluginWrapper(
        pluginDescriptor: PluginDescriptor,
        pluginPath: Path,
        pluginClassLoader: ClassLoader,
    ): PluginWrapper {
        // create the plugin wrapper
        Log.d { "Creating wrapper for plugin $pluginPath" }
        val pluginWrapper: PluginWrapper =
            PluginWrapper(this, pluginDescriptor, pluginPath, pluginClassLoader)
        pluginWrapper.setPluginFactory(pluginFactory)
        return pluginWrapper
    }

    /**
     * Tests for already loaded plugins on given path.
     *
     * @param pluginPath the path to investigate
     * @return id of plugin or null if not loaded
     */
    protected fun idForPath(pluginPath: Path): String? {
        for (plugin: PluginWrapper in pluginsMap.values) {
            if ((plugin.pluginPath == pluginPath)) {
                return plugin.pluginId
            }
        }
        return null
    }

    /**
     * Override this to change the validation criteria.
     *
     * @param descriptor the plugin descriptor to validate
     * @throws PluginRuntimeException if validation fails
     */
    protected fun validatePluginDescriptor(descriptor: PluginDescriptor) {
        if (StringUtils.isNullOrEmpty(descriptor.pluginId)) {
            throw PluginRuntimeException("Field 'id' cannot be empty")
        }
    }

    /**
     * The plugin label is used in logging and it's a string in format `pluginId@pluginVersion`.
     */
    protected fun getPluginLabel(pluginDescriptor: PluginDescriptor): String {
        return pluginDescriptor.pluginId + "@" + pluginDescriptor.version
    }

    protected fun <T> getExtensionClasses(extensionsWrapper: List<ExtensionWrapper<T>?>): List<Class<out T>> {
        val extensionClasses: MutableList<Class<out T>> = ArrayList(extensionsWrapper.size)
        for (extensionWrapper: ExtensionWrapper<T>? in extensionsWrapper) {
            extensionClasses.plus(extensionWrapper?.descriptor!!.extensionClass)
        }
        return extensionClasses
    }

    protected fun <T> getExtensions(extensionsWrapper: List<ExtensionWrapper<T>?>): List<T> {
        val extensions: MutableList<T> = ArrayList(extensionsWrapper.size)
        for (extensionWrapper: ExtensionWrapper<T>? in extensionsWrapper) {
            try {
                extensionWrapper?.extension?.let { extensions.add(it) }
            } catch (e: PluginRuntimeException) {
                Log.e { "Cannot retrieve extension $e" }
            }
        }
        return extensions
    }

    companion object {
        const val PLUGINS_DIR_PROPERTY_NAME: String = "pf4j.pluginsDir"
        const val MODE_PROPERTY_NAME: String = "pf4j.mode"
        const val DEFAULT_PLUGINS_DIR: String = "plugins"
        const val DEVELOPMENT_PLUGINS_DIR: String = "../plugins"
    }
}