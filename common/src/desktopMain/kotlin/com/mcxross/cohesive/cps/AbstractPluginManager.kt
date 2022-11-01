package com.mcxross.cohesive.cps

import com.mcxross.cohesive.common.frontend.openapi.ui.view.CohesiveView
import com.mcxross.cohesive.common.frontend.utils.isNotDirectory
import com.mcxross.cohesive.common.frontend.utils.notExists
import com.mcxross.cohesive.common.utils.Log
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import java.io.Closeable
import java.io.IOException
import java.util.*
import java.util.function.Consumer
import java.util.stream.Collectors


/**
 * This class implements the boilerplate corePlugin code that any [PluginManager]
 * implementation would have to support.
 * It helps cut the noise out of the subclass that handles corePlugin management.
 */
abstract class AbstractPluginManager : PluginManager {

  /**
   * Gets the read-only list of all paths of the folders where plugins are installed.
   *
   * @return Paths of plugins roots
   */
  final override val pluginsRoots = ArrayList<Path>()

  protected open lateinit var extensionFinder: ExtensionFinder

  protected open lateinit var pluginDescriptorFinder: PluginDescriptorFinder

  /**
   * A map of plugins this manager is responsible for (the key is the 'pluginId').
   */
  protected var pluginsMap: MutableMap<String?, PluginWrapper> = HashMap<String?, PluginWrapper>()

  /**
   * Retrieve all plugins.
   * */
  override val plugins: List<PluginWrapper>
    get() = ArrayList(pluginsMap.values)

  /**
   * A map of corePlugin class loaders (the key is the 'pluginId').
   */
  protected var pluginClassLoaders: MutableMap<String?, ClassLoader> = HashMap()

  /**
   * A list with unresolved plugins (unresolved dependency).
   */
  override var unresolvedPlugins: MutableList<PluginWrapper> = ArrayList<PluginWrapper>()

  /**
   * A list with all resolved plugins (resolved dependency).
   */
  override var resolvedPlugins: MutableList<PluginWrapper> = ArrayList<PluginWrapper>()

  /**
   * A list with started plugins.
   */
  override var startedPlugins: MutableList<PluginWrapper> = ArrayList<PluginWrapper>()

  /**
   * The registered [PluginStateListener]s.
   */
  protected var pluginStateListeners: MutableList<PluginStateListener> =
    ArrayList<PluginStateListener>()

  /**
   * Cache value for the runtime mode.
   * No need to re-read it because it won't change at runtime.
   */
  override var runtimeMode: RuntimeMode? = null
    get() {
      if (field == null) {
        // retrieves the runtime mode from system properties
        runtimeMode = RuntimeMode.byName(
          System.getProperty(
            MODE_PROPERTY_NAME,
            RuntimeMode.DEPLOYMENT.toString(),
          ),
        )
      }
      return field
    }

  /**
   * The system version used for comparisons to the corePlugin requires attribute.
   */
  final override var systemVersion: String = "0.0.0"
  protected open lateinit var pluginRepo: PluginRepository
  protected open lateinit var pluginFactory: PluginFactory
  override lateinit var extensionFactory: ExtensionFactory
  protected open lateinit var pluginStatusProvider: PluginStatusProvider
  protected open lateinit var dependencyResolver: DependencyResolver
  protected open lateinit var pluginLoader: PluginLoader
  final override val isDevelopment: Boolean
    get() = super.isDevelopment

  /**
   * Set to true to allow requires expression to be exactly x.y.z.
   * The default is false, meaning that using an exact version x.y.z will
   * implicitly mean the same as >=x.y.z
   *
   * @param exactVersionAllowed set to true or false
   */
  var isExactVersionAllowed: Boolean = false
  override lateinit var versionManager: VersionManager
  override val pluginsRoot: Path
    get() = pluginsRoots.stream()
      .findFirst()
      .orElseThrow {
        IllegalStateException(
          "pluginsRoots have not been initialized, yet.",
        )
      }
  val version: String
    get() {
      return "Cohesive.VERSION"
    }

  init {
    if (pluginsRoots.isEmpty()) {
      pluginsRoots.addAll(createPluginsRoot())
    }
  }

  constructor()

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
  }

  /**
   * Returns a copy of plugins with that state.
   */
  override fun pluginsWithState(pluginState: PluginState): List<PluginWrapper> {

    val plugins: MutableList<PluginWrapper> = ArrayList<PluginWrapper>()
    plugins.forEach {
      if (it.pluginState == pluginState) {
        plugins.add(it)
      }
    }
    return plugins

  }

  override fun getPlugin(pluginId: String): PluginWrapper {
    return pluginsMap[pluginId]!!
  }

  override fun loadPlugin(pluginPath: Path): String {
    if (!FileSystem.SYSTEM.exists(pluginPath)) {
      throw IllegalArgumentException(
        String.format(
          "Specified corePlugin %s does not exist!",
          pluginPath,
        ),
      )
    }

    Log.d { "Loading corePlugin from $pluginPath" }
    val pluginWrapper: PluginWrapper? = loadPluginFromPath(pluginPath)

    // try to resolve  the loaded corePlugin together with other possible plugins that depend on this corePlugin
    resolvePlugins()
    return pluginWrapper?.descriptor!!.pluginId
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
    pluginsRoots.forEach(
      Consumer { path: Path ->
        if (notExists(path) || isNotDirectory(path)) {
          Log.w { "No $path root" }
        }
      },
    )

    // get all corePlugin paths from repository
    val pluginPaths: List<Path> = pluginRepo.pluginPaths

    // check for no plugins
    if (pluginPaths.isEmpty()) {
      Log.i { "No plugins" }
      return
    }
    Log.d { "Found ${pluginPaths.size} possible plugins: $pluginPaths" }

    // load plugins from corePlugin paths
    pluginPaths.forEach {
      try {
        loadPluginFromPath(it)
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
    resolvedPlugins.forEach {
      unloadPlugin(it.pluginId)
    }

  }

  /**
   * Unload the specified corePlugin and it's dependents.
   */
  override fun unloadPlugin(pluginId: String): Boolean {
    return unloadPlugin(pluginId, true)
  }

  protected fun unloadPlugin(pluginId: String, unloadDependents: Boolean): Boolean {
    try {
      if (unloadDependents) {
        val dependents: MutableList<String> = dependencyResolver.getDependents(pluginId)
        while (dependents.isNotEmpty()) {
          val dependent: String = dependents.removeAt(0)
          unloadPlugin(dependent, false)
          dependents.addAll(0, dependencyResolver.getDependents(dependent))
        }
      }
      val pluginState: PluginState = stopPlugin(pluginId, false)
      if (PluginState.STARTED == pluginState) {
        return false
      }
      val pluginWrapper: PluginWrapper = getPlugin(pluginId)
      Log.i { "Unload corePlugin ${getPluginLabel(pluginWrapper.descriptor)}" }

      // remove the corePlugin
      pluginsMap.remove(pluginId)
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
    // stop the corePlugin if it's started
    val pluginState: PluginState = stopPlugin(pluginId)
    if (PluginState.STARTED == pluginState) {
      Log.e { "Failed to stop corePlugin $pluginId on delete" }
      return false
    }

    // get an instance of corePlugin before the corePlugin is unloaded
    // for reason see https://github.com/pf4j/pf4j/issues/309
    val corePlugin: CorePlugin? = pluginWrapper.corePlugin
    if (!unloadPlugin(pluginId)) {
      Log.e { "Failed to unload corePlugin $pluginId on delete" }
      return false
    }

    // notify the corePlugin as it's deleted
    corePlugin!!.delete()
    val pluginPath: Path = pluginWrapper.pluginPath
    return pluginRepo.deletePluginPath(pluginPath)
  }

  /**
   * Start all active plugins.
   */
  override fun startPlugins() {
    for (pluginWrapper: PluginWrapper in resolvedPlugins) {
      val pluginState: PluginState = pluginWrapper.pluginState
      if ((PluginState.DISABLED != pluginState) && (PluginState.STARTED != pluginState)) {
        try {
          Log.i { "Start corePlugin ${getPluginLabel(pluginWrapper.descriptor)}" }
          pluginWrapper.corePlugin!!.start()
          pluginWrapper.pluginState = PluginState.STARTED
          pluginWrapper.failedException = null
          startedPlugins.add(pluginWrapper)
        } catch (e: Exception) {
          pluginWrapper.pluginState = PluginState.FAILED
          pluginWrapper.failedException = e
          Log.e { "Unable to start corePlugin ${getPluginLabel(pluginWrapper.descriptor)}" }
        } catch (e: LinkageError) {
          pluginWrapper.pluginState = PluginState.FAILED
          pluginWrapper.failedException = e
          Log.e { "Unable to start corePlugin ${getPluginLabel(pluginWrapper.descriptor)}" }
        } finally {
          firePluginStateEvent(PluginStateEvent(this, pluginWrapper, pluginState))
        }
      }
    }
  }

  /**
   * Start the specified corePlugin and its dependencies.
   */
  override fun startPlugin(pluginId: String): PluginState {
    checkPluginId(pluginId)
    val pluginWrapper: PluginWrapper = getPlugin(pluginId)
    val pluginDescriptor: PluginDescriptor = pluginWrapper.descriptor
    val pluginState: PluginState = pluginWrapper.pluginState
    if (PluginState.STARTED == pluginState) {
      Log.d { "Already started corePlugin ${getPluginLabel(pluginDescriptor)}" }
      return PluginState.STARTED
    }
    if (!resolvedPlugins.contains(pluginWrapper)) {
      Log.w { "Cannot start an unresolved corePlugin ${getPluginLabel(pluginDescriptor)}" }
      return pluginState
    }
    if (PluginState.DISABLED == pluginState) {
      // automatically enable corePlugin on manual corePlugin start
      if (!enablePlugin(pluginId)) {
        return pluginState
      }
    }
    for (dependency: PluginDependency in pluginDescriptor.dependencies!!) {
      // start dependency only if it marked as required (non-optional) or if it optional and loaded
      if (!dependency.isOptional || pluginsMap.containsKey(dependency.pluginId)) {
        dependency.pluginId?.let { startPlugin(it) }
      }
    }
    Log.i { "Start corePlugin ${getPluginLabel(pluginDescriptor)}" }
    pluginWrapper.corePlugin!!.start()
    pluginWrapper.pluginState = PluginState.STARTED
    startedPlugins.add(pluginWrapper)
    firePluginStateEvent(PluginStateEvent(this, pluginWrapper, pluginState))
    return pluginWrapper.pluginState
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
      val pluginState: PluginState = pluginWrapper.pluginState
      if (PluginState.STARTED == pluginState) {
        try {
          Log.i { "Stop corePlugin ${getPluginLabel(pluginWrapper.descriptor)}" }
          pluginWrapper.corePlugin?.stop()
          pluginWrapper.pluginState = PluginState.STOPPED
          itr.remove()
          firePluginStateEvent(PluginStateEvent(this, pluginWrapper, pluginState))
        } catch (e: PluginRuntimeException) {
          Log.e { e.message.toString() }
        }
      }
    }
  }

  /**
   * Stop the specified corePlugin and it's dependents.
   */
  override fun stopPlugin(pluginId: String): PluginState {
    return stopPlugin(pluginId, true)
  }

  protected fun stopPlugin(pluginId: String, stopDependents: Boolean): PluginState {
    checkPluginId(pluginId)
    val pluginWrapper: PluginWrapper = getPlugin(pluginId)
    val pluginDescriptor: PluginDescriptor = pluginWrapper.descriptor
    val pluginState: PluginState = pluginWrapper.pluginState
    if (PluginState.STOPPED == pluginState) {
      Log.d { "Already stopped corePlugin ${getPluginLabel(pluginDescriptor)}" }
      return PluginState.STOPPED
    }

    // test for disabled corePlugin
    if (PluginState.DISABLED == pluginState) {
      // do nothing
      return pluginState
    }
    if (stopDependents) {
      val dependents: MutableList<String> = dependencyResolver.getDependents(pluginId)
      while (dependents.isNotEmpty()) {
        val dependent: String = dependents.removeAt(0)
        stopPlugin(dependent, false)
        dependents.addAll(0, dependencyResolver.getDependents(dependent))
      }
    }
    Log.i { "Stop corePlugin ${getPluginLabel(pluginDescriptor)}" }
    pluginWrapper.corePlugin?.stop()
    pluginWrapper.pluginState = PluginState.STOPPED
    startedPlugins.remove(pluginWrapper)
    firePluginStateEvent(PluginStateEvent(this, pluginWrapper, pluginState))
    return pluginWrapper.pluginState
  }

  protected fun checkPluginId(pluginId: String?) {
    if (!pluginsMap.containsKey(pluginId)) {
      throw IllegalArgumentException(String.format("Unknown pluginId %s", pluginId))
    }
  }

  override fun disablePlugin(pluginId: String): Boolean {
    checkPluginId(pluginId)
    val pluginWrapper: PluginWrapper = getPlugin(pluginId)
    val pluginDescriptor: PluginDescriptor = pluginWrapper.descriptor
    val pluginState: PluginState = pluginWrapper.pluginState
    if (PluginState.DISABLED == pluginState) {
      Log.d { "Already disabled corePlugin ${getPluginLabel(pluginDescriptor)}" }
      return true
    }
    if (PluginState.STOPPED == stopPlugin(pluginId)) {
      pluginWrapper.pluginState = PluginState.DISABLED
      firePluginStateEvent(PluginStateEvent(this, pluginWrapper, PluginState.STOPPED))
      pluginStatusProvider.disablePlugin(pluginId)
      Log.i { "Disabled corePlugin ${getPluginLabel(pluginDescriptor)}" }
      return true
    }
    return false
  }

  override fun enablePlugin(pluginId: String): Boolean {
    checkPluginId(pluginId)
    val pluginWrapper: PluginWrapper = getPlugin(pluginId)
    if (!isPluginValid(pluginWrapper)) {
      Log.w { "CorePlugin ${getPluginLabel(pluginWrapper.descriptor)} can not be enabled" }
      return false
    }
    val pluginDescriptor: PluginDescriptor = pluginWrapper.descriptor
    val pluginState: PluginState = pluginWrapper.pluginState
    if (PluginState.DISABLED != pluginState) {
      Log.d { "CorePlugin ${getPluginLabel(pluginDescriptor)} is not disabled" }
      return true
    }
    pluginStatusProvider.enablePlugin(pluginId)
    pluginWrapper.pluginState = PluginState.CREATED
    firePluginStateEvent(PluginStateEvent(this, pluginWrapper, pluginState))
    Log.i { "Enabled corePlugin ${getPluginLabel(pluginDescriptor)}" }
    return true
  }

  /**
   * Get the [ClassLoader] for corePlugin.
   */
  override fun getPluginClassLoader(pluginId: String): ClassLoader {
    return (pluginClassLoaders[pluginId])!!
  }

  override fun getCohesiveView(): CohesiveView? = extensionFinder.find()

  override fun getExtensionClasses(pluginId: String): List<Class<*>> {
    val extensionsWrapper: List<ExtensionWrapper<Any>?> = extensionFinder.find(pluginId)
    val extensionClasses: MutableList<Class<*>> = ArrayList(extensionsWrapper.size)
    extensionsWrapper.forEach {
      val c: Class<*> = it!!.descriptor.extensionClass
      extensionClasses.add(c)
    }
    return extensionClasses
  }

  override fun <T> getExtensionClasses(type: Class<T>): List<Class<out T>> {
    return getExtensionClasses(extensionFinder.find(type))
  }

  override fun <T> getExtensionClasses(type: Class<T>, pluginId: String): List<Class<out T>> {
    return getExtensionClasses(extensionFinder.find(type, pluginId))
  }

  override fun <T> getExtensions(type: Class<T>): List<T> {
    return getExtensions(extensionFinder.find(type))
  }

  override fun <T> getExtensions(type: Class<T>, pluginId: String): List<T> {
    return getExtensions(extensionFinder.find(type, pluginId))
  }

  override fun <T> getExtensions(pluginId: String): List<T> {
    val extensionsWrapper: List<ExtensionWrapper<T>> = extensionFinder.find(pluginId)
    val extensions: MutableList<T> = ArrayList<T>(extensionsWrapper.size)
    extensionsWrapper.forEach {
      try {
        extensions.add(it.extension!!)
      } catch (e: PluginRuntimeException) {
        Log.e { "Cannot retrieve extension" }
      }
    }

    return extensions
  }

  override fun getExtensionClassNames(pluginId: String): Set<String> {
    return extensionFinder.findClassNames(pluginId)
  }

  override fun whichPlugin(clazz: Class<*>): PluginWrapper? {
    val classLoader: ClassLoader = clazz.classLoader
    resolvedPlugins.forEach {
      if (it.pluginClassLoader === classLoader) {
        return it
      }
    }
    return null
  }

  @Synchronized
  final override fun addPluginStateListener(listener: PluginStateListener) {
    pluginStateListeners.add(listener)
  }

  @Synchronized
  override fun removePluginStateListener(listener: PluginStateListener) {
    pluginStateListeners.remove(listener)
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
      return Arrays.stream(
        pluginsDir.split(",".toRegex()).dropLastWhile { it.isEmpty() }
          .toTypedArray(),
      )
        .map { obj: String -> obj.trim { it <= ' ' } }
        .map { first: String ->
          first.toPath()
        }
        .collect(Collectors.toList())
    }
    pluginsDir = if (isDevelopment) DEVELOPMENT_PLUGINS_DIR else DEFAULT_PLUGINS_DIR
    return listOf(pluginsDir.toPath())
  }

  /**
   * Check if this corePlugin is valid (satisfies "requires" param) for a given system version.
   *
   * @param pluginWrapper the corePlugin to check
   * @return true if corePlugin satisfies the "requires" or if requires was left blank
   */
  protected fun isPluginValid(pluginWrapper: PluginWrapper): Boolean {
    var requires: String = pluginWrapper.descriptor.requires!!.trim { it <= ' ' }
    //TODO: fix regex
    if (!isExactVersionAllowed && requires.matches(Regex("^init+//.init+.init+$"))) {
      // If exact versions are not allowed in requires, rewrite to >= expression
      requires = ">=$requires"
    }
    if ((systemVersion == "0.0.0") || versionManager.checkVersionConstraint(
        systemVersion,
        requires,
      )) {
      return true
    }
    val pluginDescriptor: PluginDescriptor = pluginWrapper.descriptor
    Log.w { "CorePlugin ${getPluginLabel(pluginDescriptor)} requires a minimum system version of $requires, and you have $systemVersion" }
    return false
  }

  protected fun isPluginDisabled(pluginId: String): Boolean {
    return pluginStatusProvider.isPluginDisabled(pluginId)
  }

  protected fun resolvePlugins() {
    // retrieves the plugins descriptors
    val descriptors: MutableList<PluginDescriptor> = ArrayList<PluginDescriptor>()
    for (plugin: PluginWrapper in pluginsMap.values) {
      descriptors.add(plugin.descriptor)
    }
    val result: DependencyResolver.Result = dependencyResolver.resolve(descriptors)
    if (result.hasCyclicDependency()) {
      throw DependencyResolver.CyclicDependencyException()
    }
    val notFoundDependencies: List<String> = result.notFoundDependencies
    if (notFoundDependencies.isNotEmpty()) {
      throw DependencyResolver.DependenciesNotFoundException(notFoundDependencies)
    }
    val wrongVersionDependencies: List<DependencyResolver.WrongDependencyVersion> =
      result.wrongVersionDependencies
    if (wrongVersionDependencies.isNotEmpty()) {
      throw DependencyResolver.DependenciesWrongVersionException(wrongVersionDependencies)
    }
    val sortedPlugins: List<String> = result.sortedPlugins

    // move plugins from "unresolved" to "resolved"
    sortedPlugins.forEach {
      val pluginWrapper: PluginWrapper = pluginsMap[it]!!
      if (unresolvedPlugins.remove(pluginWrapper)) {
        val pluginState: PluginState = pluginWrapper.pluginState
        if (pluginState != PluginState.DISABLED) {
          pluginWrapper.pluginState = PluginState.RESOLVED
        }
        resolvedPlugins.add(pluginWrapper)
        Log.i { "CorePlugin ${getPluginLabel(pluginWrapper.descriptor)} resolved" }
        firePluginStateEvent(PluginStateEvent(this, pluginWrapper, pluginState))
      }
    }

  }

  @Synchronized
  protected fun firePluginStateEvent(event: PluginStateEvent) {
    pluginStateListeners.forEach {
      Log.wtf { "Fire $event to $it" }
      it.pluginStateChanged(event)
    }
  }

  protected open fun loadPluginFromPath(pluginPath: Path): PluginWrapper? {
    // Test for corePlugin path duplication
    var pluginId: String? = idForPath(pluginPath)
    if (pluginId != null) {
      throw PluginAlreadyLoadedException(pluginId, pluginPath)
    }

    // Retrieve and validate the corePlugin descriptor
    val pluginDescriptorFinder: PluginDescriptorFinder = pluginDescriptorFinder
    Log.d { "Use $pluginDescriptorFinder to find plugins descriptors" }
    Log.d { "Finding corePlugin descriptor for corePlugin $pluginPath" }
    val pluginDescriptor: PluginDescriptor = pluginDescriptorFinder.find(pluginPath)!!
    validatePluginDescriptor(pluginDescriptor)

    // Check there are no loaded plugins with the retrieved id
    pluginId = pluginDescriptor.pluginId
    if (pluginsMap.containsKey(pluginId)) {
      val loadedPlugin: PluginWrapper = getPlugin(pluginId)
      throw PluginRuntimeException(
        ("There is an already loaded corePlugin ({}) "
          + "with the same id ({}) as the corePlugin at path '{}'. Simultaneous loading "
          + "of plugins with the same PluginId is not currently supported.\n"
          + "As a workaround you may include PluginVersion and PluginProvider "
          + "in PluginId."),
        loadedPlugin, pluginId, pluginPath,
      )
    }
    Log.d { "Found descriptor $pluginDescriptor" }
    val pluginClassName: String? = pluginDescriptor.pluginClass
    Log.d { "Class $pluginClassName for corePlugin $pluginPath" }

    // load corePlugin
    Log.d { "Loading corePlugin $pluginPath" }
    val pluginClassLoader: ClassLoader = pluginLoader.loadPlugin(pluginPath, pluginDescriptor)!!
    Log.d { "Loaded corePlugin $pluginPath with class pluginLoader $pluginClassLoader" }
    val pluginWrapper: PluginWrapper =
      createPluginWrapper(pluginDescriptor, pluginPath, pluginClassLoader)

    // test for disabled corePlugin
    if (isPluginDisabled(pluginDescriptor.pluginId)) {
      Log.i { "CorePlugin $pluginPath is disabled" }
      pluginWrapper.pluginState = PluginState.DISABLED
    }

    // validate the corePlugin
    if (!isPluginValid(pluginWrapper)) {
      Log.w { "CorePlugin $pluginPath is invalid and it will be disabled" }
      pluginWrapper.pluginState = PluginState.DISABLED
    }
    Log.d { "Created wrapper $pluginWrapper for corePlugin $pluginPath" }
    pluginId = pluginDescriptor.pluginId

    // plus corePlugin to the list with plugins
    pluginsMap[pluginId] = pluginWrapper
    unresolvedPlugins.add(pluginWrapper)

    // plus corePlugin class pluginLoader to the list with class loaders
    pluginClassLoaders[pluginId] = pluginClassLoader
    return pluginWrapper
  }

  /**
   * creates the corePlugin wrapper. override this if you want to prevent plugins having full access to the corePlugin manager
   *
   * @return
   */
  protected fun createPluginWrapper(
    pluginDescriptor: PluginDescriptor,
    pluginPath: Path,
    pluginClassLoader: ClassLoader,
  ): PluginWrapper {
    // create the corePlugin wrapper
    Log.d { "Creating wrapper for corePlugin $pluginPath" }
    val pluginWrapper = PluginWrapper(this, pluginDescriptor, pluginPath, pluginClassLoader)
    pluginWrapper.pluginFactory = pluginFactory
    return pluginWrapper
  }

  /**
   * Tests for already loaded plugins on given path.
   *
   * @param pluginPath the path to investigate
   * @return id of corePlugin or null if not loaded
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
   * @param descriptor the corePlugin descriptor to validate
   * @throws PluginRuntimeException if validation fails
   */
  protected fun validatePluginDescriptor(descriptor: PluginDescriptor) {
    if (descriptor.pluginId.isEmpty()) {
      throw PluginRuntimeException("Field 'id' cannot be empty")
    }
  }

  /**
   * The corePlugin label is used in logging, and it's a string in format `pluginId@pluginVersion`.
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

  protected fun <T> getExtensions(extensionsWrapper: List<ExtensionWrapper<T>>): List<T> {
    val extensions: MutableList<T> = ArrayList()
    extensionsWrapper.forEach {
      try {
        it.extension?.let { it1 -> extensions.add(it1) }
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
