package com.mcxross.cohesive.cps


/**
 * It's a [PluginManager] that loads each plugin from a `zip` file.
 * The structure of the zip file is:
 *
 *  * `lib` directory that contains all dependencies (as jar files); it's optional (no dependencies)
 *  * `classes` directory that contains all plugin's classes
 */
class ZipPluginManager :DefaultPluginManager() {
    override fun createPluginDescriptorFinder():PluginDescriptorFinder {
        return PropertiesPluginDescriptorFinder()
    }

    override fun createPluginLoader():PluginLoader {
        return CompoundPluginLoader()
            .add(DevelopmentPluginLoader(this)) { this.isDevelopment }
            .add(DefaultPluginLoader(this)) { this.isDevelopment }
    }

    override fun createPluginRepository():PluginRepository {
        return CompoundPluginRepository()
            .add(DevelopmentPluginRepository(pluginsRoots)) { this.isDevelopment }
            .add(DefaultPluginRepository(pluginsRoots)) { this.isDevelopment }
    }
}
