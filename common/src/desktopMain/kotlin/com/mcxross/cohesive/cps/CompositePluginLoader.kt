package com.mcxross.cohesive.cps

import com.mcxross.cohesive.common.utils.Log
import okio.Path

inline fun compositePluginLoader(
  loader: CompositePluginLoader.() -> Unit,
): PluginLoader {
  val compositePluginLoader = CompositePluginLoader()
  compositePluginLoader.loader()
  return compositePluginLoader
}

class CompositePluginLoader : PluginLoader {
  private val loaders: MutableList<PluginLoader> = ArrayList()

  /**
   * Add a [PluginLoader] only if the `condition` is satisfied.
   * @param container [PluginLoaderContainer]
   */
  operator fun plus(container: PluginLoaderContainer) {
    if (container.condition.asBoolean)
      loaders.add(container.loader)
  }

  fun size(): Int {
    return loaders.size
  }

  override fun isApplicable(pluginPath: Path): Boolean {
    loaders.forEach {
      if (it.isApplicable(pluginPath)) {
        return true
      }
    }
    return false
  }

  override fun loadPlugin(pluginPath: Path, pluginDescriptor: PluginDescriptor): ClassLoader {
    loaders.forEach {
      if (it.isApplicable(pluginPath)) {
        Log.d { "$it is applicable for corePlugin $pluginPath" }
        try {
          return it.loadPlugin(pluginPath, pluginDescriptor)!!
        } catch (e: Exception) {
          // log the exception and continue with the next pluginLoader
          Log.e { e.message.toString() }
        }
      } else {
        Log.d { "$it is not applicable for corePlugin $pluginPath" }
      }
    }
    throw RuntimeException("No PluginLoader for corePlugin '$pluginPath' and descriptor '$pluginDescriptor'")
  }

}
