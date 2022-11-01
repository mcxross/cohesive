package com.mcxross.cohesive.cps.utils

import com.mcxross.cohesive.common.frontend.utils.delete
import com.mcxross.cohesive.common.frontend.utils.exists
import com.mcxross.cohesive.common.frontend.utils.isDirectory
import com.mcxross.cohesive.common.frontend.utils.isZipFile
import com.mcxross.cohesive.common.frontend.utils.isZipOrJarFile
import com.mcxross.cohesive.common.frontend.utils.lastModified
import com.mcxross.cohesive.common.frontend.utils.notExists
import com.mcxross.cohesive.common.frontend.utils.toUri
import com.mcxross.cohesive.common.utils.Log
import okio.IOException
import okio.Path
import okio.Path.Companion.toOkioPath
import java.io.File
import java.io.FileFilter
import java.net.URI
import java.nio.file.FileSystem
import java.nio.file.FileSystemNotFoundException
import java.nio.file.FileSystems

object FileUtils {

  fun getJars(folder: Path): List<File> {
    val bucket: MutableList<File> = ArrayList()
    getJars(bucket, folder)
    return bucket
  }

  private fun getJars(bucket: MutableList<File>, folder: Path) {
    val jarFilter: FileFilter = JarFileFilter()
    val directoryFilter: FileFilter = DirectoryFileFilter()
    if (isDirectory(folder)) {
      val jars = folder.toFile().listFiles(jarFilter)
      run {
        var i = 0
        while (jars != null && (i < jars.size)) {
          bucket.add(jars[i])
          ++i
        }
      }
      val directories = folder.toFile().listFiles(directoryFilter)
      var i = 0
      while (directories != null && i < directories.size) {
        val directory = directories[i]
        getJars(bucket, directory.toOkioPath())
        ++i
      }
    }
  }

  /**
   * Finds a path with various endings or null if not found.
   *
   * @param basePath the base name
   * @param endings a list of endings to search for
   * @return new path or null if not found
   */
  fun findWithEnding(basePath: Path, vararg endings: String): Path? {
    for (ending in endings) {
      val newPath = basePath.toNioPath().resolveSibling(basePath.name + ending)
      if (exists(newPath.toOkioPath())) {
        return newPath.toOkioPath()
      }
    }
    return null
  }


  fun optimisticDelete(path: Path?) {
    if (path == null) {
      return
    }
    try {
      delete(path)
    } catch (ignored: IOException) {
      // ignored
    }
  }

  /**
   * Unzip a zip file in a directory that has the same name as the zip file.
   * For example if the zip file is `my-corePlugin.zip` then the resulted directory
   * is `my-corePlugin`.
   *
   * @param filePath the file to evaluate
   * @return Path of unzipped folder or original path if this was not a zip file
   * @throws IOException on e
   */
  @Throws(IOException::class)
  fun expandIfZip(filePath: Path): Path {
    if (!isZipFile(filePath)) {
      return filePath
    }
    val fileName = filePath.name
    val directoryName = fileName.substring(0, fileName.lastIndexOf("."))
    val pluginDirectory = filePath.toNioPath().resolveSibling(directoryName).toOkioPath()
    if (notExists(pluginDirectory) || lastModified(filePath)!! > lastModified(pluginDirectory)!!) {
      // expand '.zip' file
      val unzip = Unzip(source = filePath.toFile(), destination = pluginDirectory.toFile())
      unzip.extract()
      Log.i { "Expanded corePlugin zip ${filePath.name} in ${pluginDirectory.name}" }
    }
    return pluginDirectory
  }


  @Throws(IOException::class)
  fun getPath(path: Path, first: String, vararg more: String?): Path {
    var uri = path.toUri()
    if (isZipOrJarFile(path)) {
      var pathString = path.toFile().absolutePath
      // transformation for Windows OS
      pathString = StringUtils.addStart(pathString.replace("\\", "/"), "/")
      // space is replaced with %20
      pathString = pathString.replace(" ", "%20")
      uri = URI.create("jar:file:$pathString")
    }
    return getPath(uri, first, *more)
  }

  @Throws(IOException::class)
  fun getPath(uri: URI?, first: String, vararg more: String?): Path {
    return getFileSystem(uri).getPath(first, *more).toOkioPath()
  }

  fun closePath(path: Path?) {
    if (path != null) {
      try {
        path.toNioPath().fileSystem.close()
      } catch (e: Exception) {
        // close silently
      }
    }
  }

  fun findFile(directoryPath: Path, fileName: String): Path? {
    val files = directoryPath.toFile().listFiles()
    if (files != null) {
      for (file in files) {
        if (file.isFile) {
          if (file.name == fileName) {
            return file.toOkioPath()
          }
        } else if (file.isDirectory) {
          val foundFile = findFile(file.toOkioPath(), fileName)
          if (foundFile != null) {
            return foundFile
          }
        }
      }
    }
    return null
  }

  @Throws(IOException::class)
  private fun getFileSystem(uri: URI?): FileSystem {
    return try {
      FileSystems.getFileSystem(uri)
    } catch (e: FileSystemNotFoundException) {
      FileSystems.newFileSystem(uri, emptyMap<String, String>())
    }
  }
}
