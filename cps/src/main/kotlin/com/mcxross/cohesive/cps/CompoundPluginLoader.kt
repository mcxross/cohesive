package com.mcxross.cohesive.cps

import com.mcxross.cohesive.cps.utils.Log
import java.nio.file.Path
import java.util.function.BooleanSupplier


class CompoundPluginLoader : PluginLoader {
    private val loaders: MutableList<PluginLoader> = ArrayList()
    fun add(loader: PluginLoader?): CompoundPluginLoader {
        requireNotNull(loader) { "null not allowed" }
        loaders.add(loader)
        return this
    }

    /**
     * Add a [PluginLoader] only if the `condition` is satisfied.
     *
     * @param loader
     * @param condition
     * @return
     */
    fun add(loader: PluginLoader?, condition: BooleanSupplier): CompoundPluginLoader {
        return if (condition.asBoolean) {
            add(loader)
        } else this
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
                    // log the exception and continue with the next loader
                    Log.e { e.message.toString() }
                }
            } else {
                Log.d { "$loader is not applicable for plugin $pluginPath" }

            }
        }
        throw RuntimeException("No PluginLoader for plugin '$pluginPath' and descriptor '$pluginDescriptor'")
    }
    
}