package com.mcxross.cohesive.cps

import com.mcxross.cohesive.common.frontend.utils.isDirectory
import okio.Path

/**
 * Load all information needed by a plugin from [DefaultPluginClasspath].
 */
class DefaultPluginLoader(pluginManager: PluginManager) :
  BasePluginLoader(pluginManager, DefaultPluginClasspath()) {
  override fun isApplicable(pluginPath: Path): Boolean {
    return super.isApplicable(pluginPath) && isDirectory(pluginPath)
  }
}
