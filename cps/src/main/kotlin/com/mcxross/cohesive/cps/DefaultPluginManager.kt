package com.mcxross.cohesive.cps

import com.mcxross.cohesive.cps.utils.FileUtils
import com.mcxross.cohesive.cps.utils.Log
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Default implementation of the [PluginManager] interface.
 * In essence it is a [ZipPluginManager] plus a [JarPluginManager].
 * So, it can load plugins from jar and zip, simultaneous.
 */
open class DefaultPluginManager : AbstractPluginManager {

    override var pluginRepository: PluginRepository = compositePluginRepository {
        plus(DevelopmentPluginRepository(pluginsRoots)) { isDevelopment }
        plus(JarPluginRepository(pluginsRoots)) { isDevelopment }
        plus(DefaultPluginRepository(pluginsRoots)) { isDevelopment }
    }
    override var pluginFactory: PluginFactory = DefaultPluginFactory()
    override var extensionFactory: ExtensionFactory = DefaultExtensionFactory()
    override var pluginDescriptorFinder: PluginDescriptorFinder = compositePluginDescriptorFinder {
        plus(PropertiesPluginDescriptorFinder())
        plus(ManifestPluginDescriptorFinder())
    }
    override var extensionFinder: ExtensionFinder = extensionFinder()
    override var pluginStatusProvider: PluginStatusProvider = statusProvider()
    override var pluginLoader: PluginLoader = compositePluginLoader {
        plus(DevelopmentPluginLoader(getPluginManager())) { isDevelopment }
        plus(JarPluginLoader(getPluginManager())) { isNotDevelopment }
        plus(DefaultPluginLoader(getPluginManager())) { isNotDevelopment }
    }
    override var versionManager: VersionManager? = DefaultVersionManager()
    override var dependencyResolver: DependencyResolver = dependencyResolver()

    init {
        if (isDevelopment) {
            addPluginStateListener(LoggingPluginStateListener())
        }
        Log.i { "CPS version $version in $runtimeMode mode" }
    }
    constructor()
    constructor(vararg pluginsRoots: Path) : super(*pluginsRoots)
    constructor(pluginsRoots: List<Path>) : super(pluginsRoots)

    private fun getPluginManager(): PluginManager {
        return this
    }

    private fun extensionFinder(): ExtensionFinder {
        val extensionFinder = DefaultExtensionFinder(this)
        addPluginStateListener(extensionFinder)
        return extensionFinder
    }

    private fun dependencyResolver(): DependencyResolver = versionManager?.let { DependencyResolver(it) }!!

    private fun statusProvider(): PluginStatusProvider {
        val configDir = System.getProperty(PLUGINS_DIR_CONFIG_PROPERTY_NAME)
        val configPath = if (configDir != null) Paths.get(configDir) else pluginsRoots.stream()
            .findFirst()
            .orElseThrow {
                IllegalArgumentException(
                    "No pluginsRoot configured"
                )
            }
        return DefaultPluginStatusProvider(configPath)
    }

    /**
     * Load a plugin from disk. If the path is a zip file, first unpack.
     *
     * @param pluginPath plugin location on disk
     * @return PluginWrapper for the loaded plugin or null if not loaded
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
        const val PLUGINS_DIR_CONFIG_PROPERTY_NAME = "pf4j.pluginsConfigDir"
    }
}