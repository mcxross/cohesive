package xyz.mcxross.cohesive.cps

import xyz.mcxross.cohesive.utils.Log
import xyz.mcxross.cohesive.cps.utils.AndFileFilter
import xyz.mcxross.cohesive.cps.utils.DirectoryFileFilter
import xyz.mcxross.cohesive.cps.utils.FileUtils
import xyz.mcxross.cohesive.cps.utils.HiddenFilter
import xyz.mcxross.cohesive.cps.utils.NotFileFilter
import xyz.mcxross.cohesive.cps.utils.OrFileFilter
import xyz.mcxross.cohesive.cps.utils.ZipFileFilter
import java.io.File
import java.io.FileFilter
import java.io.IOException
import okio.Path
import okio.Path.Companion.toPath

class DefaultPluginRepository(pluginsRoots: List<Path>) : BasePluginRepository(pluginsRoots) {

  override var filter: FileFilter? = filter()
  override val pluginPaths: List<Path>
    get() {
      extractZipFiles()
      return super.pluginPaths
    }

  constructor(vararg pluginsRoots: Path) : this(mutableListOf<Path>(*pluginsRoots)) {}

  fun filter(): AndFileFilter {
    val pluginsFilter = AndFileFilter(DirectoryFileFilter())
    pluginsFilter.fileFilters.add(NotFileFilter(createHiddenPluginFilter()))
    return pluginsFilter
  }

  override fun deletePluginPath(pluginPath: Path): Boolean {
    FileUtils.optimisticDelete(
      FileUtils.findWithEnding(
        pluginPath,
        ".zip",
        ".ZIP",
        ".Zip",
      ),
    )
    return super.deletePluginPath(pluginPath)
  }

  protected fun createHiddenPluginFilter(): FileFilter {
    return OrFileFilter(HiddenFilter())
  }

  private fun extractZipFiles() {
    // expand plugins zip files
    pluginsRoots
      .stream()
      .flatMap { path: Path? ->
        streamFiles(
          path!!,
          ZipFileFilter,
        )
      }
      .map { obj: File -> obj.absolutePath.toPath() }
      .forEach { filePath: Path? -> expandIfZip(filePath!!) }
  }

  private fun expandIfZip(filePath: Path) {
    try {
      FileUtils.expandIfZip(filePath)
    } catch (e: IOException) {
      Log.e { "Cannot expand Plugin Zip $filePath" }
      Log.e { e.message.toString() }
    }
  }
}
