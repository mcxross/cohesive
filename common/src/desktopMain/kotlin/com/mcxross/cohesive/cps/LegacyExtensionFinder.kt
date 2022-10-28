package com.mcxross.cohesive.cps

import com.mcxross.cohesive.common.utils.Log
import java.io.IOException


/**
 * An extension finder that attempts to find System and Plugin Extensions.
 *
 * All extensions declared in a plugin are indexed in various kt files `com/mcxross/cohesive/r/{DefaultCohesiveExtension || DefaultExtensionIndex}`.
 * This class looks-up extensions in all extensions index files i.e System and Plugins.
 */
class LegacyExtensionFinder(pluginManager: PluginManager) : AbstractExtensionFinder(pluginManager) {

  override fun readSystemExtensionIndex(): MutableMap<String, Set<String>> {
    Log.d { "Reading extensions storages from classpath" }
    val result: MutableMap<String, Set<String>> = LinkedHashMap()
    val bucket = mutableSetOf<String>()
    try {
      (javaClass.classLoader.loadClass(COHESIVE_RESOURCE).getDeclaredConstructor()
        .newInstance() as ExtensionIndex).extensions.forEach {
        bucket.add(it)
      }
      (javaClass.classLoader.loadClass(EXTENSIONS_RESOURCE).getDeclaredConstructor()
        .newInstance() as ExtensionIndex).extensions.forEach {
        bucket.add(it)
      }
      debugExtensions(bucket)
      result["cohesive"] = bucket
    } catch (e: IOException) {
      Log.e { e.message.toString() }
    }
    return result
  }

  override fun readPluginExtensionIndex(): Map<String, Set<String>> {
    Log.d { "Reading extensions storages from plugins" }
    val result: MutableMap<String, Set<String>> = LinkedHashMap()
    pluginManager.plugins.forEach {
      val pluginId: String = it.pluginId
      Log.d { "Reading extensions storages from plugin $pluginId" }
      val bucket = mutableSetOf<String>()
      try {
        Log.d { "Read $EXTENSIONS_RESOURCE" }
        (javaClass.classLoader.loadClass(EXTENSIONS_RESOURCE).getDeclaredConstructor()
          .newInstance() as ExtensionIndex).extensions.forEach { ext ->
          bucket.add(ext)
        }
        debugExtensions(bucket)
        result[pluginId] = bucket
      } catch (e: IOException) {
        Log.e { e.message.toString() }
      }
    }

    return result
  }

  companion object {
    const val EXTENSIONS_RESOURCE: String = "com.mcxross.cohesive.r.DefaultExtensionIndex"
    const val COHESIVE_RESOURCE: String = "com.mcxross.cohesive.r.DefaultCohesiveExtension"
  }
}
