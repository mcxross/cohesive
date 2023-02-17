package com.mcxross.cohesive.cps

/**
 * It's a [PluginManager] that loads each Plugin from a `zip` file. The structure of the zip file
 * is:
 *
 * * `lib` directory that contains all dependencies (as jar files); it's optional (no dependencies)
 * * `classes` directory that contains all Plugin's classes
 */
class ZipPluginManager : DefaultPluginManager() {

  override var pluginDescriptorFinder: PluginDescriptorFinder = PropertiesPluginDescriptorFinder()
  val pluginManager = this
  override var pluginLoader: PluginLoader = compositePluginLoader {
    this +
      PluginLoaderContainer(
        loader = DevelopmentPluginLoader(pluginManager),
        condition = { isDevelopment },
      )
    this +
      PluginLoaderContainer(
        loader = DefaultPluginLoader(pluginManager),
        condition = { isDevelopment },
      )
  }

  override var pluginRepo: PluginRepository = compositePluginRepository {
    this +
      PluginRepositoryContainer(
        repo = DevelopmentPluginRepository(pluginsRoots),
        condition = { isDevelopment },
      )
    this +
      PluginRepositoryContainer(
        repo = DefaultPluginRepository(pluginsRoots),
        condition = { isDevelopment },
      )
  }
}
