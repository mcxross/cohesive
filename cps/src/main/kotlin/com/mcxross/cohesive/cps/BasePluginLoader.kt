package com.mcxross.cohesive.cps

import com.mcxross.cohesive.cps.utils.FileUtils
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

/**
 * Load all information needed by a plugin.
 * This means plus to the plugin's [ClassLoader] all the jar files and
 * all the class files specified in the [PluginClasspath].
 */
open class BasePluginLoader(pluginManager: PluginManager, pluginClasspath: PluginClasspath) :
    PluginLoader {
    protected var pluginManager: PluginManager
    protected var pluginClasspath: PluginClasspath

    init {
        this.pluginManager = pluginManager
        this.pluginClasspath = pluginClasspath
    }

    override fun isApplicable(pluginPath: Path): Boolean {
        return Files.exists(pluginPath)
    }

    override fun loadPlugin(pluginPath: Path, pluginDescriptor: PluginDescriptor): ClassLoader {
        val pluginClassLoader: PluginClassLoader = createPluginClassLoader(pluginPath, pluginDescriptor)
        loadClasses(pluginPath, pluginClassLoader)
        loadJars(pluginPath, pluginClassLoader)
        return pluginClassLoader
    }

    protected fun createPluginClassLoader(
        pluginPath: Path,
        pluginDescriptor: PluginDescriptor,
    ): PluginClassLoader {
        return PluginClassLoader(pluginManager, pluginDescriptor, javaClass.classLoader)
    }

    /**
     * Add all `*.class` files from [PluginClasspath.getClassesDirectories]
     * to the plugin's [ClassLoader].
     */
    protected fun loadClasses(pluginPath: Path, pluginClassLoader: PluginClassLoader) {
        for (directory in pluginClasspath.getClassesDirectories()) {
            val file = pluginPath.resolve(directory).toFile()
            if (file.exists() && file.isDirectory) {
                pluginClassLoader.addFile(file)
            }
        }
    }

    /**
     * Add all `*.jar` files from [PluginClasspath.getJarsDirectories]
     * to the plugin's [ClassLoader].
     */
    protected fun loadJars(pluginPath: Path, pluginClassLoader: PluginClassLoader) {
        for (jarsDirectory in pluginClasspath.getJarsDirectories()) {
            val file = pluginPath.resolve(jarsDirectory)
            val jars: List<File> = FileUtils.getJars(file)
            for (jar in jars) {
                pluginClassLoader.addFile(jar)
            }
        }
    }
}