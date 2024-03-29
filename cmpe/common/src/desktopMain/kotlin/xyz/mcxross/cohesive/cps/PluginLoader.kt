package xyz.mcxross.cohesive.cps

import okio.Path

/** Load all information (classes) needed by a Plugin. */
interface PluginLoader {
  /**
   * Returns true if this pluginLoader is applicable to the given [Path].
   *
   * @param pluginPath
   * @return
   */
  fun isApplicable(pluginPath: Path): Boolean
  fun loadPlugin(pluginPath: Path, pluginDescriptor: PluginDescriptor): ClassLoader?
}
