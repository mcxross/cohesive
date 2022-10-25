package com.mcxross.cohesive.cps

import com.mcxross.cohesive.common.frontend.utils.*
import com.mcxross.cohesive.common.utils.Log
import java.io.IOException
import java.nio.file.Files
import java.util.jar.JarFile
import java.util.jar.Manifest
import java.util.zip.ZipFile
import com.mcxross.cohesive.cps.utils.FileUtils
import com.mcxross.cohesive.cps.utils.StringUtils
import com.mcxross.cohesive.cps.utils.isNotNullOrEmpty
import okio.Path

/**
 * Read the plugin descriptor from the manifest file.
 */
class ManifestPluginDescriptorFinder : PluginDescriptorFinder {
    override fun isApplicable(pluginPath: Path): Boolean {
        return exists(pluginPath) && (isDirectory(pluginPath) || isZipOrJarFile(
            pluginPath
        ))
    }

    override fun find(pluginPath: Path): PluginDescriptor {
        val manifest = readManifest(pluginPath)
        return createPluginDescriptor(manifest)
    }

    protected fun readManifest(pluginPath: Path): Manifest {
        if (isJarFile(pluginPath)) {
            return readManifestFromJar(pluginPath)
        }
        return if (isZipFile(pluginPath)) {
            readManifestFromZip(pluginPath)
        } else readManifestFromDirectory(pluginPath)
    }

    protected fun createPluginDescriptor(manifest: Manifest): PluginDescriptor {
        val pluginDescriptor: DefaultPluginDescriptor = createPluginDescriptorInstance()

        // TODO validate !!!
        val attributes = manifest.mainAttributes
        val id = attributes.getValue(PLUGIN_ID)
        pluginDescriptor.pluginId = id
        val description = attributes.getValue(PLUGIN_DESCRIPTION)
        if (description.isNullOrEmpty()) {
            pluginDescriptor.pluginDescription = ""
        } else {
            pluginDescriptor.pluginDescription = description
        }
        val clazz = attributes.getValue(PLUGIN_CLASS)
        if (clazz.isNotNullOrEmpty()) {
            pluginDescriptor.pluginClass = clazz
        }
        val version = attributes.getValue(PLUGIN_VERSION)
        if (version.isNotNullOrEmpty()) {
            pluginDescriptor.version = version
        }
        val provider = attributes.getValue(PLUGIN_PROVIDER)
        pluginDescriptor.provider = provider
        val dependencies = attributes.getValue(PLUGIN_DEPENDENCIES)
        pluginDescriptor.setDependencies(dependencies)
        val requires = attributes.getValue(PLUGIN_REQUIRES)
        if (requires.isNotNullOrEmpty()) {
            pluginDescriptor.requires = requires
        }
        pluginDescriptor.license = attributes.getValue(PLUGIN_LICENSE)
        return pluginDescriptor
    }

    protected fun createPluginDescriptorInstance(): DefaultPluginDescriptor {
        return DefaultPluginDescriptor()
    }

    protected fun readManifestFromJar(jarPath: Path): Manifest {
        try {
            JarFile(jarPath.toFile()).use { jar -> return jar.manifest }
        } catch (e: IOException) {
            throw PluginRuntimeException(e, "Cannot read manifest from {}", jarPath)
        }
    }

    protected fun readManifestFromZip(zipPath: Path): Manifest {
        try {
            ZipFile(zipPath.toFile()).use { zip ->
                val manifestEntry = zip.getEntry("classes/META-INF/MANIFEST.MF")
                zip.getInputStream(manifestEntry).use { manifestInput -> return Manifest(manifestInput) }
            }
        } catch (e: IOException) {
            throw PluginRuntimeException(e, "Cannot read manifest from {}", zipPath)
        }
    }

    protected fun readManifestFromDirectory(pluginPath: Path): Manifest {
        // legacy (the path is something like "classes/META-INF/MANIFEST.MF")
        val manifestPath: Path = FileUtils.findFile(pluginPath, "MANIFEST.MF")
            ?: throw PluginRuntimeException("Cannot find the manifest path")
        Log.d { "Lookup plugin descriptor in $manifestPath" }
        if (notExists(manifestPath)) {
            throw PluginRuntimeException("Cannot find '{}' path", manifestPath)
        }
        try {
            newInputStream(manifestPath).use { input  -> return Manifest(input) }
        } catch (e: IOException) {
            throw PluginRuntimeException(e, "Cannot read manifest from {}", pluginPath)
        }
    }

    companion object {
        const val PLUGIN_ID = "Plugin-Id"
        const val PLUGIN_DESCRIPTION = "Plugin-Description"
        const val PLUGIN_CLASS = "Plugin-Class"
        const val PLUGIN_VERSION = "Plugin-Version"
        const val PLUGIN_PROVIDER = "Plugin-Provider"
        const val PLUGIN_DEPENDENCIES = "Plugin-Dependencies"
        const val PLUGIN_REQUIRES = "Plugin-Requires"
        const val PLUGIN_LICENSE = "Plugin-License"
    }
}