package xyz.mcxross.cohesive.cps

import xyz.mcxross.cohesive.common.utils.Log
import java.io.IOException

/**
 * An extension finder that attempts to find System and Plugin Extensions.
 *
 * All extensions declared in a Plugin are indexed in various kt files
 * `xyz/mcxross/cohesive/r/{DefaultCohesiveExtension || DefaultExtensionIndex}`. This class looks-up
 * extensions in all extensions index files i.e. System and Plugins.
 */
class LegacyExtensionFinder(pluginManager: PluginManager) : AbstractExtensionFinder(pluginManager) {

  override fun readSystemExtensionIndex(): MutableMap<String, Set<String>> {
    Log.d { "Reading Extensions' Storages from System Classpath" }
    val result: MutableMap<String, Set<String>> = LinkedHashMap()
    val bucket = mutableSetOf<String>()
    try {
      (javaClass.classLoader.loadClass(COHESIVE_RESOURCE).getDeclaredConstructor().newInstance()
          as ExtensionIndex)
        .extensions
        .forEach { bucket.add(it) }
    } catch (e: ClassNotFoundException) {
      Log.e { e.message.toString() }
    }

    try {
      (javaClass.classLoader.loadClass(EXTENSIONS_RESOURCE).getDeclaredConstructor().newInstance()
          as ExtensionIndex)
        .extensions
        .forEach { bucket.add(it) }
    } catch (e: Exception) {
      Log.d { e.message.toString() }
    }

    debugExtensions(bucket)
    result["cohesive"] = bucket

    return result
  }

  override fun readPluginExtensionIndex(): Map<String, Set<String>> {
    Log.d { "Reading Extensions Storages from Plugins" }
    val result: MutableMap<String, Set<String>> = LinkedHashMap()
    pluginManager.plugins.forEach {
      val pluginId: String = it.pluginId
      Log.d { "Reading Extensions Storages from Plugin $pluginId" }
      val bucket = mutableSetOf<String>()
      try {
        Log.d { "Determining if $pluginId is Secondary Plugin" }
        Log.d { "Read $COHESIVE_RESOURCE" }
        (it.pluginClassLoader.loadClass(COHESIVE_RESOURCE).getDeclaredConstructor().newInstance()
            as ExtensionIndex)
          .extensions
          .forEach { ext -> bucket.add(ext) }
      } catch (e: IOException) {
        Log.d { "$pluginId is Not a Secondary Plugin" }
      /*try {
          (javaClass.classLoader
              .loadClass(EXTENSIONS_RESOURCE)
              .getDeclaredConstructor()
              .newInstance() as ExtensionIndex)
            .extensions
            .forEach { ext -> bucket.add(ext) }
        } catch (e: IOException) {
          Log.e { e.message.toString() }
        }*/
      }
      debugExtensions(bucket)
      result[pluginId] = bucket
    }

    return result
  }

  companion object {
    const val EXTENSIONS_RESOURCE: String = "xyz.mcxross.cohesive.r.DefaultExtensionIndex"
    const val COHESIVE_RESOURCE: String = "xyz.mcxross.cohesive.r.DefaultCohesiveExtension"
  }
}
