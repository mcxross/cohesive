package com.mcxross.cohesive.cps

import com.mcxross.cohesive.common.frontend.utils.exists
import com.mcxross.cohesive.common.frontend.utils.isJarFile
import okio.Path

class JarPluginLoader(var pluginManager: PluginManager) : PluginLoader {

    override fun isApplicable(pluginPath: Path): Boolean {
        return exists(pluginPath) && isJarFile(pluginPath)
    }

    override fun loadPlugin(pluginPath: Path, pluginDescriptor: PluginDescriptor): ClassLoader {
        val pluginClassLoader = PluginClassLoader(pluginManager, pluginDescriptor, javaClass.classLoader)
        pluginClassLoader.addFile(pluginPath.toFile())
        return pluginClassLoader
    }
}