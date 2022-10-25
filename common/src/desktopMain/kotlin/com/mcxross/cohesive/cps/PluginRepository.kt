package com.mcxross.cohesive.cps

import okio.Path


/**
 * Directory that contains plugins. A plugin could be a `directory`, @code zip} or `jar` file.
 */
interface PluginRepository {
    /**
     * List all plugin paths.
     *
     * @return a list with paths
     */
    val pluginPaths: List<Path>

    /**
     * Removes a plugin from the repository.
     *
     * @param pluginPath the plugin path
     * @return true if deleted
     * @throws PluginRuntimeException if something goes wrong
     */
    fun deletePluginPath(pluginPath: Path): Boolean
}