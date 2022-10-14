package com.mcxross.cohesive.cps


/**
 * It's a [PluginManager] that loads each plugin from a `zip` file.
 * The structure of the zip file is:
 *
 *  * `lib` directory that contains all dependencies (as jar files); it's optional (no dependencies)
 *  * `classes` directory that contains all plugin's classes
 */
class ZipPluginManager : DefaultPluginManager() {

    override var pluginDescriptorFinder: PluginDescriptorFinder = PropertiesPluginDescriptorFinder()

    override var pluginLoader: PluginLoader = CompositePluginLoader()
        .plus(DevelopmentPluginLoader(this)) { this.isDevelopment }
        .plus(DefaultPluginLoader(this)) { this.isDevelopment }

    override var pluginRepository: PluginRepository = compositePluginRepository {
        plus(DevelopmentPluginRepository(pluginsRoots)) { isDevelopment }
        plus(DefaultPluginRepository(pluginsRoots)) { isDevelopment }
    }

}
