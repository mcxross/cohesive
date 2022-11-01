package com.mcxross.cohesive.cps

import com.mcxross.cohesive.common.frontend.utils.exists
import com.mcxross.cohesive.cps.utils.FileUtils
import okio.Path
import java.io.File

/**
 * Load all information needed by a corePlugin.
 * This means plus to the corePlugin's [ClassLoader] all the jar files and
 * all the class files specified in the [PluginClasspath].
 */
open class BasePluginLoader(
  var pluginManager: PluginManager,
  var pluginClasspath: PluginClasspath
) :
  PluginLoader {

  override fun isApplicable(pluginPath: Path): Boolean {
    return exists(pluginPath)
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
   * to the corePlugin's [ClassLoader].
   */
  protected fun loadClasses(pluginPath: Path, pluginClassLoader: PluginClassLoader) {
    pluginClasspath.classesDirectories.forEach {
      val file = pluginPath.resolve(it).toFile()
      if (file.exists() && file.isDirectory) {
        pluginClassLoader.addFile(file)
      }
    }
  }

  /**
   * Add all `*.jar` files from [PluginClasspath.getJarsDirectories]
   * to the corePlugin's [ClassLoader].
   */
  protected fun loadJars(pluginPath: Path, pluginClassLoader: PluginClassLoader) {
    pluginClasspath.jarsDirectories.forEach {
      val file = pluginPath.resolve(it)
      val jars: List<File> = FileUtils.getJars(file)
      for (jar in jars) {
        pluginClassLoader.addFile(jar)
      }
    }
  }
}
