package com.mcxross.cohesive.cps

import java.nio.file.Files
import java.nio.file.Path

/**
 * Load all information needed by a holder from [DefaultPluginClasspath].
 */
class DefaultPluginLoader(pluginManager: PluginManager) :
    BasePluginLoader(pluginManager, DefaultPluginClasspath()) {
    override fun isApplicable(pluginPath: Path): Boolean {
        return super.isApplicable(pluginPath) && Files.isDirectory(pluginPath)
    }
}
