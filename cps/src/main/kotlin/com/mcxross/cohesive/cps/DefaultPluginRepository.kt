package com.mcxross.cohesive.cps

import com.mcxross.cohesive.cps.utils.*
import java.io.File
import java.io.FileFilter
import java.io.IOException
import java.nio.file.Path


class DefaultPluginRepository(pluginsRoots: List<Path>) : BasePluginRepository(pluginsRoots) {
    constructor(vararg pluginsRoots: Path) : this(mutableListOf<Path>(*pluginsRoots)) {}

    override var filter: FileFilter? = filter()
    fun filter(): AndFileFilter {
        val pluginsFilter =
            AndFileFilter(DirectoryFileFilter())
        pluginsFilter.fileFilters.add(NotFileFilter(createHiddenPluginFilter()))
        return pluginsFilter
    }

    override val pluginPaths: List<Path>
        get() {
            extractZipFiles()
            return super.pluginPaths
        }

    override fun deletePluginPath(pluginPath: Path): Boolean {
        FileUtils.optimisticDelete(
            FileUtils.findWithEnding(
                pluginPath,
                ".zip",
                ".ZIP",
                ".Zip"
            )
        )
        return super.deletePluginPath(pluginPath)
    }

    protected fun createHiddenPluginFilter(): FileFilter {
        return OrFileFilter(HiddenFilter())
    }

    private fun extractZipFiles() {
        // expand plugins zip files

        pluginsRoots.stream()
            .flatMap { path: Path? ->
                streamFiles(
                    path!!, ZipFileFilter
                )
            }
            .map { obj: File -> obj.toPath() }
            .forEach { filePath: Path? -> expandIfZip(filePath!!) }
    }

    private fun expandIfZip(filePath: Path) {
        try {
            FileUtils.expandIfZip(filePath)
        } catch (e: IOException) {
            Log.e { "Cannot expand holder zip $filePath" }
            Log.e { e.message.toString() }
        }
    }


}