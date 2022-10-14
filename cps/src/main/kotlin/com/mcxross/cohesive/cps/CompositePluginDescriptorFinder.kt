package com.mcxross.cohesive.cps

import com.mcxross.cohesive.cps.utils.Log
import java.nio.file.Path

fun compositePluginDescriptorFinder(
    descriptor: CompositePluginDescriptorFinder.() -> Unit,
) : PluginDescriptorFinder {
    val finder = CompositePluginDescriptorFinder()
    descriptor(finder)
    return finder
}

/**
 * A [PluginDescriptorFinder] that delegates to a list of other [PluginDescriptorFinder]s.
 * */
class CompositePluginDescriptorFinder : PluginDescriptorFinder {

    private val finders: MutableList<PluginDescriptorFinder> = ArrayList()
    fun plus(finder: PluginDescriptorFinder): CompositePluginDescriptorFinder {
        finders.add(finder)
        return this
    }

    fun size(): Int {
        return finders.size
    }

    override fun isApplicable(pluginPath: Path): Boolean {
        for (finder in finders) {
            if (finder.isApplicable(pluginPath)) {
                return true
            }
        }
        return false
    }

    override fun find(pluginPath: Path): PluginDescriptor {
        for (finder in finders) {
            if (finder.isApplicable(pluginPath)) {
                Log.d { "$finder is applicable for plugin $pluginPath" }
                try {
                    return finder.find(pluginPath)!!
                } catch (e: Exception) {
                    if (finders.indexOf(finder) == finders.size - 1) {
                        // it's the last finder
                        Log.e { e.message.toString() }
                    } else {
                        // log the exception and continue with the next finder
                        Log.d { e.message.toString() }
                        Log.d { "Try to continue with the next finder" }
                    }
                }
            } else {
                Log.d { "$finder is not applicable for plugin $pluginPath" }
            }
        }
        throw PluginRuntimeException("No PluginDescriptorFinder for plugin '{}'", pluginPath)
    }
}