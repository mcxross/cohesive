package xyz.mcxross.cohesive.cps

import xyz.mcxross.cohesive.utils.exists
import xyz.mcxross.cohesive.utils.isJarFile
import okio.Path

class JarPluginLoader(var pluginManager: PluginManager) : PluginLoader {

  override fun isApplicable(pluginPath: Path): Boolean {
    return exists(pluginPath) && isJarFile(pluginPath)
  }

  override fun loadPlugin(pluginPath: Path, pluginDescriptor: PluginDescriptor): ClassLoader {
    val pluginClassLoader =
      PluginClassLoader(pluginManager, pluginDescriptor, javaClass.classLoader)
    pluginClassLoader.addFile(pluginPath.toFile())
    return pluginClassLoader
  }
}
