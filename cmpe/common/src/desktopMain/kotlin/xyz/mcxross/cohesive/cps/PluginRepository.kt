package xyz.mcxross.cohesive.cps

import okio.Path

/**
 * Directory that contains plugins. A Plugin could be a `directory`, @code zip} or `jar` file.
 */
interface PluginRepository {
  /**
   * List all Plugin paths.
   *
   * @return a list with paths
   */
  val pluginPaths: List<Path>

  /**
   * Removes a Plugin from the repository.
   *
   * @param pluginPath the Plugin path
   * @return true if deleted
   * @throws PluginRuntimeException if something goes wrong
   */
  fun deletePluginPath(pluginPath: Path): Boolean
}
