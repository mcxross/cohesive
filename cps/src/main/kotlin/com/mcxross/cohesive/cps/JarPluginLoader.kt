package com.mcxross.cohesive.cps

import com.mcxross.cohesive.cps.utils.FileUtils
import java.nio.file.Files
import java.nio.file.Path


class JarPluginLoader(var pluginManager: PluginManager) : PluginLoader {

    override fun isApplicable(pluginPath: Path): Boolean {
        return Files.exists(pluginPath) && FileUtils.isJarFile(pluginPath)
    }

    override fun loadPlugin(pluginPath: Path, pluginDescriptor: PluginDescriptor): ClassLoader {
        val pluginClassLoader = PluginClassLoader(pluginManager, pluginDescriptor, javaClass.classLoader)
        pluginClassLoader.addFile(pluginPath.toFile())
        return pluginClassLoader
    }
}