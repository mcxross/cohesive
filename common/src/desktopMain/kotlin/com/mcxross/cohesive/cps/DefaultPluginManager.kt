package com.mcxross.cohesive.cps

import com.mcxross.cohesive.common.utils.Log
import com.mcxross.cohesive.cps.utils.FileUtils
import okio.Path
import okio.Path.Companion.toPath

/**
 * DSL-style API for [DefaultPluginManager] interaction.
 */
fun pluginManager(managerAction: DefaultPluginManager.() -> Unit): DefaultPluginManager {
  val pluginManager = DefaultPluginManager()
  pluginManager.managerAction()
  return pluginManager
}

/**
 * Default implementation of the [PluginManager] interface.
 *
 * In essence, it is a [ZipPluginManager] plus a [JarPluginManager].
 * So, it can load plugins from jar and zip, simultaneous.
 */
open class DefaultPluginManager : AbstractPluginManager {

  override var pluginRepo: PluginRepository = compositePluginRepository {
    this + PluginRepositoryContainer(
      repo = DevelopmentPluginRepository(pluginsRoots),
      condition = { isDevelopment },
    )
    this + PluginRepositoryContainer(
      repo = JarPluginRepository(pluginsRoots),
      condition = { isNotDevelopment },
    )
    this + PluginRepositoryContainer(
      repo = DefaultPluginRepository(pluginsRoots),
      condition = { isNotDevelopment },
    )
  }
  override var pluginFactory: PluginFactory = DefaultPluginFactory()
  override var extensionFactory: ExtensionFactory = DefaultExtensionFactory()
  override var pluginDescriptorFinder: PluginDescriptorFinder = compositePluginDescriptorFinder {
    this + PropertiesPluginDescriptorFinder()
    this + ManifestPluginDescriptorFinder()
  }
  override var extensionFinder: ExtensionFinder = extensionFinder()
  override var pluginStatusProvider: PluginStatusProvider = statusProvider()
  override var pluginLoader: PluginLoader = compositePluginLoader {
    this + PluginLoaderContainer(
      loader = DevelopmentPluginLoader(getPluginManager()),
      condition = { isDevelopment },
    )
    this + PluginLoaderContainer(
      loader = JarPluginLoader(getPluginManager()),
      condition = { isNotDevelopment },
    )
    this + PluginLoaderContainer(
      loader = DefaultPluginLoader(getPluginManager()),
      condition = { isNotDevelopment },
    )
  }
  override var versionManager: VersionManager = DefaultVersionManager()
  override var dependencyResolver: DependencyResolver = dependencyResolver()

  init {
    if (isDevelopment) {
      addPluginStateListener(LoggingPluginStateListener())
    }
    Log.i { "CPS version $version in $runtimeMode mode" }
  }

  constructor() : super()
  constructor(vararg pluginsRoots: Path) : super(*pluginsRoots)
  constructor(pluginsRoots: List<Path>) : super(pluginsRoots)

  private fun getPluginManager(): PluginManager {
    return this
  }

  private fun extensionFinder(): ExtensionFinder {
    val extensionFinder = CompositeExtensionFinder(this)
    addPluginStateListener(extensionFinder)
    return extensionFinder
  }

  private fun dependencyResolver(): DependencyResolver = DependencyResolver(versionManager)

  private fun statusProvider(): PluginStatusProvider {
    val configDir = System.getProperty(PLUGINS_DIR_CONFIG_PROPERTY_NAME)
    val configPath = configDir?.toPath()
      ?: pluginsRoots.stream()
        .findFirst()
        .orElseThrow {
          IllegalArgumentException(
            "No pluginsRoot configured",
          )
        }
    return DefaultPluginStatusProvider(configPath)
  }

  /**
   * Load a corePlugin from disk. If the path is a zip file, first unpack.
   *
   * @param pluginPath corePlugin location on disk
   * @return PluginWrapper for the loaded corePlugin or null if not loaded
   * @throws PluginRuntimeException if problems during load
   */
  override fun loadPluginFromPath(pluginPath: Path): PluginWrapper? {
    // First unzip any ZIP files
    var pluginPathin = pluginPath
    pluginPathin = try {
      FileUtils.expandIfZip(pluginPath)
    } catch (e: Exception) {
      Log.w { "Failed to unzip $pluginPath $e" }
      return null
    }
    return super.loadPluginFromPath(pluginPathin)
  }

  companion object {
    const val PLUGINS_DIR_CONFIG_PROPERTY_NAME = "cps.pluginsConfigDir"
  }
}
