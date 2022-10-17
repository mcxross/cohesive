package com.mcxross.cohesive.cps

import com.mcxross.cohesive.cps.utils.Log
import java.io.IOException

/**
 * All extensions declared in a plugin are indexed in a kt file `com/mcxross/cohesive/r/Extensions`.
 * This class lookup extensions in all extensions index files `com/mcxross/cohesive/r/Extensions`.
 */
class LegacyExtensionFinder(pluginManager: PluginManager) : AbstractExtensionFinder(pluginManager) {
    override fun readClasspathStorages(): MutableMap<String, Set<String>> {
        Log.d { "Reading extensions storages from classpath" }
        val result: MutableMap<String, Set<String>> = LinkedHashMap()
        val bucket = mutableSetOf<String>()
        try {
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

    override fun readPluginsStorages(): Map<String, Set<String>> {
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
    }
}