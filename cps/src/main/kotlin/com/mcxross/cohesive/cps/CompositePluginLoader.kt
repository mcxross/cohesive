package com.mcxross.cohesive.cps

import com.mcxross.cohesive.cps.utils.Log
import java.nio.file.Path

fun compositePluginLoader(
    loader: CompositePluginLoader.() -> Unit,
): PluginLoader {
    val compositePluginLoader = CompositePluginLoader()
    loader(compositePluginLoader)
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
        for (loader in loaders) {
            if (loader.isApplicable(pluginPath)) {
                return true
            }
        }
        return false
    }

    override fun loadPlugin(pluginPath: Path, pluginDescriptor: PluginDescriptor): ClassLoader {
        for (loader in loaders) {
            if (loader.isApplicable(pluginPath)) {
                Log.d { "$loader is applicable for plugin $pluginPath" }
                try {
                    return loader.loadPlugin(pluginPath, pluginDescriptor)!!
                } catch (e: Exception) {
                    // log the exception and continue with the next pluginLoader
                    Log.e { e.message.toString() }
                }
            } else {
                Log.d { "$loader is not applicable for plugin $pluginPath" }

            }
        }
        throw RuntimeException("No PluginLoader for plugin '$pluginPath' and descriptor '$pluginDescriptor'")
    }

}