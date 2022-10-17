package com.mcxross.cohesive.cps

import com.mcxross.cohesive.cps.utils.Log
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.*

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
        val plugins: List<Any?> = pluginManager.plugins
        for (plugin in plugins) {
            val pluginId: String = (plugin as PluginWrapper).pluginId
            Log.d { "Reading extensions storages from plugin $pluginId" }
            val bucket: Set<String> = HashSet()
            try {
                Log.d { "Read $EXTENSIONS_RESOURCE" }
                val pluginClassLoader: ClassLoader = plugin.pluginClassLoader
                pluginClassLoader.getResourceAsStream(EXTENSIONS_RESOURCE).use { resourceStream ->
                    if (resourceStream == null) {
                        Log.d { "Cannot find $EXTENSIONS_RESOURCE" }
                    } else {
                        collectExtensions(resourceStream, bucket)
                    }
                }
                debugExtensions(bucket)
                result[pluginId] = bucket
            } catch (e: IOException) {
                Log.e { e.message.toString() }
            }
        }
        return result
    }

    @Throws(IOException::class)
    private fun collectExtensions(urls: Enumeration<URL>, bucket: Set<String>) {
        while (urls.hasMoreElements()) {
            val url = urls.nextElement()
            Log.d { "Read ${url.file}" }
            collectExtensions(url.openStream(), bucket)
        }
    }

    @Throws(IOException::class)
    private fun collectExtensions(inputStream: InputStream, bucket: Set<String>) {
        InputStreamReader(inputStream, StandardCharsets.UTF_8).use { reader ->
            /*ExtensionStorage.read(
                reader,
                bucket.toMutableSet()
            )*/
        }
    }

    companion object {
        const val EXTENSIONS_RESOURCE: String = "com.mcxross.cohesive.r.DefaultExtensionIndex"
    }
}