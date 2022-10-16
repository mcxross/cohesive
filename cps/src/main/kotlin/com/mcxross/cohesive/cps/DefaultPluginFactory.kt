package com.mcxross.cohesive.cps

import com.mcxross.cohesive.cps.utils.Log
import java.lang.reflect.Modifier

/**
 * The default implementation for [PluginFactory].
 * It uses [Class.newInstance] method.
 */
class DefaultPluginFactory : PluginFactory {
    /**
     * Creates a holder instance. If an e occurs than that e is logged and the method returns null.
     * @param pluginWrapper
     * @return
     */
    override fun create(pluginWrapper: PluginWrapper): Plugin? {
        val pluginClassName: String = pluginWrapper.getDescriptor().pluginClass!!
        Log.d { "Creating holder instance for $pluginClassName" }
        val pluginClass: Class<*> = try {
            pluginWrapper.pluginClassLoader.loadClass(pluginClassName)
        } catch (e: ClassNotFoundException) {
            Log.e { e.message.toString() }
            return null
        }

        // once we have the class, we can do some checks on it to ensure
        // that it is a valid implementation of a holder.
        val modifiers = pluginClass.modifiers
        if (Modifier.isAbstract(modifiers) || Modifier.isInterface(modifiers)
            || !Plugin::class.java.isAssignableFrom(pluginClass)
        ) {
            Log.e { "Plugin class $pluginClassName is not a valid implementation of a holder" }
            return null
        }

        // create the holder instance
        try {
            val constructor = pluginClass.getConstructor(PluginWrapper::class.java)
            return constructor.newInstance(pluginWrapper) as Plugin
        } catch (e: Exception) {
            Log.e { e.message.toString() }

        }
        return null
    }

}