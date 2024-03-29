package xyz.mcxross.cohesive.cps

import xyz.mcxross.cohesive.utils.Log
import xyz.mcxross.cohesive.cps.utils.FileUtils
import java.io.IOException
import java.net.URISyntaxException
import java.net.URL
import java.nio.charset.StandardCharsets
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import java.util.*
import kotlin.io.path.toPath
import okio.Path.Companion.toOkioPath

/**
 * The [java.util.ServiceLoader] base implementation for [ExtensionFinder]. This class lookup
 * extensions in all extensions index files `META-INF/services`.
 */
class ServiceProviderExtensionFinder(pluginManager: PluginManager) :
  AbstractExtensionFinder(pluginManager) {
  override fun readSystemExtensionIndex(): MutableMap<String, Set<String>> {
    Log.d { "Reading classpath storages" }
    val result: MutableMap<String, Set<String>> = LinkedHashMap()
    val bucket: MutableSet<String> = HashSet()
    try {
      val urls = javaClass.classLoader.getResources(EXTENSIONS_RESOURCE)
      if (urls.hasMoreElements()) {
        collectExtensions(urls, bucket)
      } else {
        Log.d { "No extensions found in classpath $EXTENSIONS_RESOURCE" }
      }
      debugExtensions(bucket)
      result["cohesive"] = bucket
    } catch (e: IOException) {
      Log.e { "Error reading extensions from classpath ${e.message.toString()}" }
    } catch (e: URISyntaxException) {
      Log.e { "Error reading extensions from classpath ${e.message.toString()}" }
    }
    return result
  }

  override fun readPluginExtensionIndex(): Map<String, Set<String>> {
    Log.d { "Reading extensions storages from plugins" }
    val result: MutableMap<String, Set<String>> = LinkedHashMap()
    val plugins: List<PluginWrapper> = (pluginManager.plugins as List<PluginWrapper>?)!!
    for (plugin in plugins) {
      val pluginId: String = plugin.descriptor.pluginId
      Log.d { "Reading extensions storages from Plugin $pluginId" }
      val bucket: MutableSet<String> = HashSet()
      try {
        val urls: Enumeration<URL> =
          (plugin.pluginClassLoader as PluginClassLoader).findResources(
            EXTENSIONS_RESOURCE,
          )
        if (urls.hasMoreElements()) {
          collectExtensions(urls, bucket)
        } else {
          Log.d { "Cannot find $EXTENSIONS_RESOURCE" }
        }
        debugExtensions(bucket)
        result[pluginId] = bucket
      } catch (e: IOException) {
        Log.e { "Error reading extensions from Plugin $pluginId ${e.message.toString()}" }
      } catch (e: URISyntaxException) {
        Log.e { "Error reading extensions from Plugin $pluginId ${e.message.toString()}" }
      }
    }
    return result
  }

  @Throws(URISyntaxException::class, IOException::class)
  private fun collectExtensions(urls: Enumeration<URL>, bucket: MutableSet<String>) {
    while (urls.hasMoreElements()) {
      val url = urls.nextElement()
      Log.d { "Read ${url.file}" }
      collectExtensions(url, bucket)
    }
  }

  @Throws(URISyntaxException::class, IOException::class)
  private fun collectExtensions(url: URL, bucket: MutableSet<String>) {
    val extensionPath: Path =
      if (url.toURI().scheme == "jar") {
        FileUtils.getPath(url.toURI(), EXTENSIONS_RESOURCE).toNioPath()
      } else {
        url.toURI().toPath()
      }
    try {
      bucket.addAll(readExtensions(extensionPath))
    } finally {
      FileUtils.closePath(extensionPath.toOkioPath())
    }
  }

  @Throws(IOException::class)
  private fun readExtensions(extensionPath: Path): Set<String> {
    val result: Set<String> = HashSet()
    Files.walkFileTree(
      extensionPath,
      emptySet(),
      1,
      object : SimpleFileVisitor<Path?>() {
        @Throws(IOException::class)
        override fun visitFile(file: Path?, attrs: BasicFileAttributes?): FileVisitResult {
          Log.d { "Read extension file $file" }
          Files.newBufferedReader(file!!, StandardCharsets.UTF_8).use { reader ->
            /*ExtensionStorage.read(
                reader,
                result.toMutableSet(),
            )*/
          }
          return FileVisitResult.CONTINUE
        }
      },
    )
    return result
  }

  companion object {

    val EXTENSIONS_RESOURCE: String = "ServiceProviderExtensionStorage.EXTENSIONS_RESOURCE"
  }
}
