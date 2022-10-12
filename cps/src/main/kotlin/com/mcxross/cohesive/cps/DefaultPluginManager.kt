package com.mcxross.cohesive.cps

import com.mcxross.cohesive.cps.utils.FileUtils
import com.mcxross.cohesive.cps.utils.Log
import java.nio.file.Path
import java.nio.file.Paths
import java.util.function.BooleanSupplier
import java.util.function.Supplier


/**
 * Default implementation of the [PluginManager] interface.
 * In essence it is a [ZipPluginManager] plus a [JarPluginManager].
 * So, it can load plugins from jar and zip, simultaneous.
 */
open class DefaultPluginManager : AbstractPluginManager {
    constructor() : super() {}
    constructor(vararg pluginsRoots: Path) : super(*pluginsRoots) {}
    constructor(pluginsRoots: List<Path>) : super(pluginsRoots) {}

    override fun createPluginDescriptorFinder(): PluginDescriptorFinder {
        return CompoundPluginDescriptorFinder()
            .add(PropertiesPluginDescriptorFinder())
            .add(ManifestPluginDescriptorFinder())
    }

    override fun createExtensionFinder(): ExtensionFinder {
        val extensionFinder = DefaultExtensionFinder(this)
        addPluginStateListener(extensionFinder)
        return extensionFinder
    }

    override fun createPluginFactory(): PluginFactory {
        return DefaultPluginFactory()
    }

    override fun createExtensionFactory(): ExtensionFactory {
        return DefaultExtensionFactory()
    }

    override fun createPluginStatusProvider(): PluginStatusProvider {
        val configDir = System.getProperty(PLUGINS_DIR_CONFIG_PROPERTY_NAME)
        val configPath = if (configDir != null) Paths.get(configDir) else pluginsRoots.stream()
            .findFirst()
            .orElseThrow<IllegalArgumentException>(Supplier {
                IllegalArgumentException(
                    "No pluginsRoot configured"
                )
            })
        return DefaultPluginStatusProvider(configPath)
    }

    override fun createPluginRepository(): PluginRepository {
        return CompoundPluginRepository()
            .add(DevelopmentPluginRepository(pluginsRoots)) { this.isDevelopment }
            .add(JarPluginRepository(pluginsRoots)) { this.isDevelopment }
            .add(DefaultPluginRepository(pluginsRoots)) { this.isDevelopment }
    }

    protected override fun createPluginLoader(): PluginLoader {
        return CompoundPluginLoader()
            .add(DevelopmentPluginLoader(this)) { this.isDevelopment }
            .add(JarPluginLoader(this)) { this.isNotDevelopment }
            .add(DefaultPluginLoader(this)) { this.isNotDevelopment }
    }

    override fun createVersionManager(): VersionManager {
        return DefaultVersionManager()
    }

    override fun initialize() {
        super.initialize()
        if (isDevelopment) {
            addPluginStateListener(LoggingPluginStateListener())
        }
        Log.i { "CPS version $version in $runtimeMode mode" }
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