package com.mcxross.cohesive.cps

import java.nio.file.Path


/**
 * Load all information (classes) needed by a plugin.
 */
interface PluginLoader {
    /**
     * Returns true if this loader is applicable to the given [Path].
     *
     * @param pluginPath
     * @return
     */
    fun isApplicable(pluginPath: Path): Boolean
    fun loadPlugin(pluginPath: Path, pluginDescriptor: PluginDescriptor): ClassLoader?
}