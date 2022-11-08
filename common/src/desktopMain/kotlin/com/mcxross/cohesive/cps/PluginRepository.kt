package com.mcxross.cohesive.cps

import okio.Path

/**
 * Directory that contains plugins. A corePlugin could be a `directory`, @code zip} or `jar` file.
 */
interface PluginRepository {
  /**
   * List all corePlugin paths.
   *
   * @return a list with paths
   */
  val pluginPaths: List<Path>

  /**
   * Removes a corePlugin from the repository.
   *
   * @param pluginPath the corePlugin path
   * @return true if deleted
   * @throws PluginRuntimeException if something goes wrong
   */
  fun deletePluginPath(pluginPath: Path): Boolean
}
