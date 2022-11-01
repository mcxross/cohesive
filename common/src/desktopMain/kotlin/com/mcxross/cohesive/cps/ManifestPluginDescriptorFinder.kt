package com.mcxross.cohesive.cps

import com.mcxross.cohesive.common.frontend.utils.exists
import com.mcxross.cohesive.common.frontend.utils.isDirectory
import com.mcxross.cohesive.common.frontend.utils.isJarFile
import com.mcxross.cohesive.common.frontend.utils.isZipFile
import com.mcxross.cohesive.common.frontend.utils.isZipOrJarFile
import com.mcxross.cohesive.common.frontend.utils.newInputStream
import com.mcxross.cohesive.common.frontend.utils.notExists
import com.mcxross.cohesive.common.utils.Log
import com.mcxross.cohesive.cps.utils.FileUtils
import com.mcxross.cohesive.cps.utils.isNotNullOrEmpty
import okio.Path
import java.io.IOException
import java.util.jar.JarFile
import java.util.jar.Manifest
import java.util.zip.ZipFile

/**
 * Read the corePlugin descriptor from the manifest file.
 */
class ManifestPluginDescriptorFinder : PluginDescriptorFinder {
  override fun isApplicable(pluginPath: Path): Boolean {
    return exists(pluginPath) && (isDirectory(pluginPath) || isZipOrJarFile(
      pluginPath,
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
    val id = attributes.getValue(CorePLUGIN_ID)
    pluginDescriptor.pluginId = id
    val description = attributes.getValue(CorePLUGIN_DESCRIPTION)
    if (description.isNullOrEmpty()) {
      pluginDescriptor.pluginDescription = ""
    } else {
      pluginDescriptor.pluginDescription = description
    }
    val clazz = attributes.getValue(CorePLUGIN_CLASS)
    if (clazz.isNotNullOrEmpty()) {
      pluginDescriptor.pluginClass = clazz
    }
    val version = attributes.getValue(CorePLUGIN_VERSION)
    if (version.isNotNullOrEmpty()) {
      pluginDescriptor.version = version
    }
    val provider = attributes.getValue(CorePLUGIN_PROVIDER)
    pluginDescriptor.provider = provider
    val dependencies = attributes.getValue(CorePLUGIN_DEPENDENCIES)
    pluginDescriptor.setDependencies(dependencies)
    val requires = attributes.getValue(CorePLUGIN_REQUIRES)
    if (requires.isNotNullOrEmpty()) {
      pluginDescriptor.requires = requires
    }
    pluginDescriptor.license = attributes.getValue(CorePLUGIN_LICENSE)
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
    Log.d { "Lookup corePlugin descriptor in $manifestPath" }
    if (notExists(manifestPath)) {
      throw PluginRuntimeException("Cannot find '{}' path", manifestPath)
    }
    try {
      newInputStream(manifestPath).use { input -> return Manifest(input) }
    } catch (e: IOException) {
      throw PluginRuntimeException(e, "Cannot read manifest from {}", pluginPath)
    }
  }

  companion object {
    const val CorePLUGIN_ID = "CorePlugin-Id"
    const val CorePLUGIN_DESCRIPTION = "CorePlugin-Description"
    const val CorePLUGIN_CLASS = "CorePlugin-Class"
    const val CorePLUGIN_VERSION = "CorePlugin-Version"
    const val CorePLUGIN_PROVIDER = "CorePlugin-Provider"
    const val CorePLUGIN_DEPENDENCIES = "CorePlugin-Dependencies"
    const val CorePLUGIN_REQUIRES = "CorePlugin-Requires"
    const val CorePLUGIN_LICENSE = "CorePlugin-License"
  }
}
