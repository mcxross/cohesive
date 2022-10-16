package com.mcxross.cohesive.cps

import java.nio.file.Path

/**
 * Directory that contains plugins. A holder could be a `directory`, @code zip} or `jar` file.
 */
interface PluginRepository {
    /**
     * List all holder paths.
     *
     * @return a list with paths
     */
    val pluginPaths: List<Path>

    /**
     * Removes a holder from the repository.
     *
     * @param pluginPath the holder path
     * @return true if deleted
     * @throws PluginRuntimeException if something goes wrong
     */
    fun deletePluginPath(pluginPath: Path): Boolean
}