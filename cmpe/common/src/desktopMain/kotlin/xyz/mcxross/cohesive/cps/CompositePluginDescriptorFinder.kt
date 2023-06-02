package xyz.mcxross.cohesive.cps

import xyz.mcxross.cohesive.common.utils.Log
import okio.Path

inline fun compositePluginDescriptorFinder(
  descriptor: CompositePluginDescriptorFinder.() -> Unit,
): PluginDescriptorFinder {
  val compositePluginDescriptorFinder = CompositePluginDescriptorFinder()
  compositePluginDescriptorFinder.descriptor()
  return compositePluginDescriptorFinder
}

/** A [PluginDescriptorFinder] that delegates to a list of other [PluginDescriptorFinder]s. */
class CompositePluginDescriptorFinder : PluginDescriptorFinder {

  private val finders: MutableList<PluginDescriptorFinder> = ArrayList()

  operator fun plus(finder: PluginDescriptorFinder) {
    finders.add(finder)
  }

  fun size(): Int {
    return finders.size
  }

  override fun isApplicable(pluginPath: Path): Boolean {
    finders.forEach {
      if (it.isApplicable(pluginPath)) {
        return true
      }
    }
    return false
  }

  override fun find(pluginPath: Path): PluginDescriptor {
    finders.forEach {
      if (it.isApplicable(pluginPath)) {
        Log.d { "${it::class} is applicable for Plugin $pluginPath" }
        try {
          return it.find(pluginPath)!!
        } catch (e: Exception) {
          if (finders.indexOf(it) == finders.size - 1) {
            // it's the last finder
            Log.e { e.message.toString() }
          } else {
            // log the exception and continue with the next finder
            Log.d { e.message.toString() }
            Log.d { "Try to continue with the next Finder" }
          }
        }
      } else {
        Log.d { "${it::class} is not applicable for Plugin $pluginPath" }
      }
    }
    throw PluginRuntimeException("No PluginDescriptorFinder for Plugin '{}'", pluginPath)
  }
}
