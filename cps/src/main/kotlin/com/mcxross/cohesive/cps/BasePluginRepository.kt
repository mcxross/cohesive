package com.mcxross.cohesive.cps

import com.mcxross.cohesive.cps.utils.FileUtils
import java.io.File
import java.io.FileFilter
import java.io.IOException
import java.nio.file.NoSuchFileException
import java.nio.file.Path
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream

open class BasePluginRepository @JvmOverloads constructor(
    protected val pluginsRoots: List<Path>,
    protected open var filter: FileFilter? = null,
) : PluginRepository {

    var comparator: Comparator<File>? = Comparator.comparingLong { obj: File -> obj.lastModified() }
    override val pluginPaths: List<Path>
        get() = pluginsRoots.stream()
            .flatMap { path: Path ->
                streamFiles(
                    path,
                    filter
                )
            }
            .sorted(comparator)
            .map { obj: File -> obj.toPath() }
            .collect(Collectors.toList())

    constructor(vararg pluginsRoots: Path) : this(listOf<Path>(*pluginsRoots)) {}

    override fun deletePluginPath(pluginPath: Path): Boolean {
        return if (!filter!!.accept(pluginPath.toFile())) {
            false
        } else try {
            FileUtils.delete(pluginPath)
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