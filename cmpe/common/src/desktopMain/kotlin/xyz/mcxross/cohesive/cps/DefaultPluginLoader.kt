package xyz.mcxross.cohesive.cps

import xyz.mcxross.cohesive.utils.isDirectory
import okio.Path

/** Load all information needed by a Plugin from [DefaultPluginClasspath]. */
class DefaultPluginLoader(pluginManager: PluginManager) :
  BasePluginLoader(pluginManager, DefaultPluginClasspath()) {
  override fun isApplicable(pluginPath: Path): Boolean {
    return super.isApplicable(pluginPath) && isDirectory(pluginPath)
  }
}
