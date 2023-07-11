package xyz.mcxross.cohesive.cps

import xyz.mcxross.cohesive.utils.exists
import xyz.mcxross.cohesive.utils.isDir
import xyz.mcxross.cohesive.utils.isDirectory
import xyz.mcxross.cohesive.utils.isZipOrJarFile
import xyz.mcxross.cohesive.utils.newInputStream
import xyz.mcxross.cohesive.utils.notExists
import xyz.mcxross.cohesive.utils.Log
import xyz.mcxross.cohesive.cps.utils.FileUtils
import xyz.mcxross.cohesive.cps.utils.isNotNullOrEmpty
import java.io.IOException
import java.util.*
import okio.Path

/** Find a Plugin descriptor in a properties file (in Plugin repository). */
class PropertiesPluginDescriptorFinder
@JvmOverloads
constructor(protected var propertiesFileName: String = DEFAULT_PROPERTIES_FILE_NAME) :
  PluginDescriptorFinder {
  override fun isApplicable(pluginPath: Path): Boolean {
    return exists(pluginPath) &&
      (isDirectory(pluginPath) ||
        isZipOrJarFile(
          pluginPath,
        ))
  }

  override fun find(pluginPath: Path): PluginDescriptor {
    val properties = readProperties(pluginPath)
    return createPluginDescriptor(properties)
  }

  protected fun readProperties(pluginPath: Path): Properties {
    val propertiesPath = getPropertiesPath(pluginPath, propertiesFileName)
    val properties = Properties()
    try {
      Log.d { "Reading properties from $propertiesPath" }
      if (notExists(propertiesPath)) {
        throw PluginRuntimeException("Cannot find '{}' path", propertiesPath)
      }
      try {
        newInputStream(propertiesPath).use { properties.load(it) }
      } catch (e: IOException) {
        throw PluginRuntimeException(e)
      }
    } finally {
      FileUtils.closePath(propertiesPath)
    }
    return properties
  }

  protected fun getPropertiesPath(pluginPath: Path, propertiesFileName: String): Path {
    return if (pluginPath.isDir()) {
      pluginPath.resolve(propertiesFileName)
    } else
      try {
        FileUtils.getPath(pluginPath, propertiesFileName)
      } catch (e: IOException) {
        throw PluginRuntimeException(e)
      }

    // it's a zip or jar file
  }

  protected fun createPluginDescriptor(properties: Properties): PluginDescriptor {
    val pluginDescriptor: DefaultPluginDescriptor = createPluginDescriptorInstance()

    // TODO validate !!!
    val id = properties.getProperty(PLUGIN_ID)
    pluginDescriptor.pluginId = id
    val description = properties.getProperty(PLUGIN_DESCRIPTION)
    if (description.isNullOrEmpty()) {
      pluginDescriptor.pluginDescription = ""
    } else {
      pluginDescriptor.pluginDescription = description
    }
    val clazz = properties.getProperty(PLUGIN_CLASS)
    if (clazz.isNotNullOrEmpty()) {
      pluginDescriptor.pluginClass = clazz
    }
    val version = properties.getProperty(PLUGIN_VERSION)
    if (version.isNotNullOrEmpty()) {
      pluginDescriptor.version = version
    }
    val provider = properties.getProperty(PLUGIN_PROVIDER)
    pluginDescriptor.provider = provider
    val dependencies = properties.getProperty(PLUGIN_DEPENDENCIES)
    pluginDescriptor.setDependencies(dependencies)
    val requires = properties.getProperty(PLUGIN_REQUIRES)
    if (requires.isNotNullOrEmpty()) {
      pluginDescriptor.requires = requires
    }
    pluginDescriptor.license = properties.getProperty(PLUGIN_LICENSE)
    return pluginDescriptor
  }

  protected fun createPluginDescriptorInstance(): DefaultPluginDescriptor {
    return DefaultPluginDescriptor()
  }

  companion object {
    const val DEFAULT_PROPERTIES_FILE_NAME = "Plugin.properties"
    const val PLUGIN_ID = "Plugin.id"
    const val PLUGIN_DESCRIPTION = "Plugin.description"
    const val PLUGIN_CLASS = "Plugin.class"
    const val PLUGIN_VERSION = "Plugin.version"
    const val PLUGIN_PROVIDER = "Plugin.provider"
    const val PLUGIN_DEPENDENCIES = "Plugin.dependencies"
    const val PLUGIN_REQUIRES = "Plugin.requires"
    const val PLUGIN_LICENSE = "Plugin.license"
  }
}
