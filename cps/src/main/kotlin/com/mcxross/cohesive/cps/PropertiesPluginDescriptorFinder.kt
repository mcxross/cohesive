package com.mcxross.cohesive.cps

import com.mcxross.cohesive.cps.utils.FileUtils
import com.mcxross.cohesive.cps.utils.Log
import com.mcxross.cohesive.cps.utils.StringUtils
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.*
import java.util.*


/**
 * Find a holder descriptor in a properties file (in holder repository).
 */
class PropertiesPluginDescriptorFinder @JvmOverloads constructor(protected var propertiesFileName: String = DEFAULT_PROPERTIES_FILE_NAME) :
    PluginDescriptorFinder {
    override fun isApplicable(pluginPath: Path): Boolean {
        return Files.exists(pluginPath) && (Files.isDirectory(pluginPath) || FileUtils.isZipOrJarFile(
            pluginPath
        ))
    }

    override fun find(pluginPath: Path): PluginDescriptor {
        val properties = readProperties(pluginPath)
        return createPluginDescriptor(properties)
    }

    protected fun readProperties(pluginPath: Path): Properties {
        val propertiesPath = getPropertiesPath(pluginPath, propertiesFileName)
            ?: throw PluginRuntimeException("Cannot find the properties path")
        val properties = Properties()
        try {
            Log.d { "Reading properties from $propertiesPath" }
            if (Files.notExists(propertiesPath)) {
                throw PluginRuntimeException("Cannot find '{}' path", propertiesPath)
            }
            try {
                Files.newInputStream(propertiesPath).use { input -> properties.load(input) }
            } catch (e: IOException) {
                throw PluginRuntimeException(e)
            }
        } finally {
            FileUtils.closePath(propertiesPath)
        }
        return properties
    }

    protected fun getPropertiesPath(pluginPath: Path, propertiesFileName: String): Path {
        return if (Files.isDirectory(pluginPath)) {
            pluginPath.resolve(Paths.get(propertiesFileName))
        } else try {
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
        if (StringUtils.isNullOrEmpty(description)) {
            pluginDescriptor.pluginDescription = ""
        } else {
            pluginDescriptor.pluginDescription = description
        }
        val clazz = properties.getProperty(PLUGIN_CLASS)
        if (StringUtils.isNotNullOrEmpty(clazz)) {
            pluginDescriptor.pluginClass = clazz
        }
        val version = properties.getProperty(PLUGIN_VERSION)
        if (StringUtils.isNotNullOrEmpty(version)) {
            pluginDescriptor.version = version
        }
        val provider = properties.getProperty(PLUGIN_PROVIDER)
        pluginDescriptor.provider = provider
        val dependencies = properties.getProperty(PLUGIN_DEPENDENCIES)
        pluginDescriptor.setDependencies(dependencies)
        val requires = properties.getProperty(PLUGIN_REQUIRES)
        if (StringUtils.isNotNullOrEmpty(requires)) {
            pluginDescriptor.requires = requires
        }
        pluginDescriptor.license = properties.getProperty(PLUGIN_LICENSE)
        return pluginDescriptor
    }

    protected fun createPluginDescriptorInstance(): DefaultPluginDescriptor {
        return DefaultPluginDescriptor()
    }

    companion object {
        const val DEFAULT_PROPERTIES_FILE_NAME = "holder.properties"
        const val PLUGIN_ID = "holder.id"
        const val PLUGIN_DESCRIPTION = "holder.description"
        const val PLUGIN_CLASS = "holder.class"
        const val PLUGIN_VERSION = "holder.version"
        const val PLUGIN_PROVIDER = "holder.provider"
        const val PLUGIN_DEPENDENCIES = "holder.dependencies"
        const val PLUGIN_REQUIRES = "holder.requires"
        const val PLUGIN_LICENSE = "holder.license"
    }
}