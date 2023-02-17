package com.mcxross.cohesive.cps

import com.mcxross.cohesive.common.frontend.utils.exists
import com.mcxross.cohesive.common.utils.Log
import com.mcxross.cohesive.cps.utils.FileUtils
import java.io.File
import okio.Path

/**
 * Load all information needed by a Plugin. This means plus to the Plugin's [ClassLoader] all the
 * jar files and all the class files specified in the [PluginClasspath].
 */
open class BasePluginLoader(
  var pluginManager: PluginManager,
  var pluginClasspath: PluginClasspath
) : PluginLoader {

  override fun isApplicable(pluginPath: Path): Boolean {
    return exists(pluginPath)
  }

  override fun loadPlugin(pluginPath: Path, pluginDescriptor: PluginDescriptor): ClassLoader {
    val pluginClassLoader: PluginClassLoader = createPluginClassLoader(pluginPath, pluginDescriptor)

    try {
      loadClasses(pluginPath, pluginClassLoader)
    } catch (e: Exception) {
      Log.e { e.message.toString() }
    }

    // Dependencies are optional; could therefore be null - try
    try {
      loadJars(pluginPath, pluginClassLoader)
    } catch (e: Exception) {
      // log the exception and continue with the next pluginLoader
      Log.d {
        "libs dir in Plugin ${pluginDescriptor.pluginId} path is probably null, proceeding without. " +
          e.message.toString()
      }
    }
    return pluginClassLoader
  }

  protected fun createPluginClassLoader(
    pluginPath: Path,
    pluginDescriptor: PluginDescriptor,
  ): PluginClassLoader {
    return PluginClassLoader(pluginManager, pluginDescriptor, javaClass.classLoader)
  }

  /**
   * Add all `*.class` files from [PluginClasspath.classesDirectories] to the Plugin's [ClassLoader]
   * .
   */
  protected fun loadClasses(pluginPath: Path, pluginClassLoader: PluginClassLoader) {
    pluginClasspath.classesDirectories.forEach {
      val file = pluginPath.resolve(it).toFile()
      if (file.exists() && file.isDirectory) {
        pluginClassLoader.addFile(file)
      }
    }
  }

  /** Add all `*.jar` files from [PluginClasspath.jarsDirectories] to the Plugin's [ClassLoader]. */
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
