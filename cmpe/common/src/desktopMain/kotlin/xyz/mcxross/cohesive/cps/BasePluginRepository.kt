package xyz.mcxross.cohesive.cps

import xyz.mcxross.cohesive.utils.delete
import okio.IOException
import okio.Path
import okio.Path.Companion.toPath
import java.io.File
import java.io.FileFilter
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream

open class BasePluginRepository
@JvmOverloads
constructor(
  protected val pluginsRoots: List<Path>,
  protected open var filter: FileFilter? = null,
) : PluginRepository {

  var comparator: Comparator<File>? = Comparator.comparingLong { obj: File -> obj.lastModified() }
  override val pluginPaths: List<Path>
    get() =
      pluginsRoots
        .stream()
        .flatMap { path: Path ->
          streamFiles(
            path,
            filter,
          )
        }
        .sorted(comparator)
        .map { obj: File -> obj.absolutePath.toPath() }
        .collect(Collectors.toList())

  constructor(vararg pluginsRoots: Path) : this(listOf<Path>(*pluginsRoots))

  override fun deletePluginPath(pluginPath: Path): Boolean {
    return if (!filter!!.accept(pluginPath.toFile())) {
      false
    } else
      try {
        delete(pluginPath)
        true
      } catch (e: NoSuchFileException) {
        false // Return false on not found to be compatible with previous API (#135)
      } catch (e: IOException) {
        throw PluginRuntimeException(e)
      }
  }

  protected fun streamFiles(directory: Path, filter: FileFilter?): Stream<File> {
    val files = directory.toFile().listFiles(filter)
    return if (files != null) Arrays.stream(files) else Stream.empty()
  }
}
