package xyz.mcxross.cohesive.common.frontend.utils

import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import okio.buffer
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.URI
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

actual fun readFileToStr(
  path: String,
): String {
  val file = FileSystem.SYSTEM.source(path.toPath())
  return file.buffer().readUtf8()
}

fun exists(path: String): Boolean {
  return FileSystem.SYSTEM.exists(path.toPath())
}

fun exists(path: Path): Boolean {
  return FileSystem.SYSTEM.exists(path)
}

fun notExists(path: String): Boolean {
  return !exists(path)
}

fun notExists(path: Path): Boolean {
  return !exists(path)
}

fun isDirectory(path: String): Boolean {
  return FileSystem.SYSTEM.metadata(path.toPath()).isDirectory
}

fun isDirectory(path: Path): Boolean {
  return FileSystem.SYSTEM.metadata(path).isDirectory
}

fun Path.isDir(): Boolean {
  return FileSystem.SYSTEM.metadata(this).isDirectory
}

fun isNotDirectory(path: String): Boolean {
  return !isDirectory(path)
}

fun isNotDirectory(path: Path): Boolean {
  return !isDirectory(path)
}

fun isFile(path: String): Boolean {
  return FileSystem.SYSTEM.metadata(path.toPath()).isRegularFile
}

/**
 * Delete a file (not recursively) and ignore any errors.
 *
 * @param path the path to delete specified as a string
 */
fun delete(path: String) {
  FileSystem.SYSTEM.delete(path.toPath())
}

/**
 * Delete a file (not recursively) and ignore any errors.
 *
 * @param path the path to delete specified as a [Path]
 */
fun delete(path: Path) {
  FileSystem.SYSTEM.delete(path)
}

fun lastModified(path: String): Long? {
  return FileSystem.SYSTEM.metadata(path.toPath()).lastModifiedAtMillis
}

fun lastModified(path: Path): Long? {
  return FileSystem.SYSTEM.metadata(path).lastModifiedAtMillis
}


/**
 * Delete a file or recursively delete a folder, do not follow symlinks.
 *
 * @param path the file or folder to delete specified as a [Path]
 */
fun deleteRecursively(path: String) {
  FileSystem.SYSTEM.deleteRecursively(path.toPath())
}

/**
 * Delete a file or recursively delete a folder, do not follow symlinks.
 *
 * @param path the file or folder to delete specified as a [Path]
 */
fun deleteRecursively(path: Path) {
  FileSystem.SYSTEM.deleteRecursively(path)
}


/**
 * Return true only if path is a zip file.
 *
 * @param path to a file/dir specified as a string
 * @return true if file with `.zip` ending
 */
fun isZipFile(path: String): Boolean {
  return isRegularFile(path) && path.endsWith(".zip", true)
}

/**
 * Return true only if path is a zip file.
 *
 * @param path to a file/dir specified as a [Path]
 * @return true if file with `.zip` ending
 */
fun isZipFile(path: Path): Boolean {
  return isRegularFile(path) && path.toString().endsWith(".zip", true)
}


/**
 * Return true only if path is a jar file.
 *
 * @param path to a file/dir specified as a string
 * @return true if file with `.jar` ending
 */
fun isJarFile(path: String): Boolean {
  return isRegularFile(path) && path.endsWith(".jar", true)
}

/**
 * Return true only if path is a jar file.
 *
 * @param path to a file/dir specified as a [Path]
 * @return true if file with `.jar` ending
 */
fun isJarFile(path: Path): Boolean {
  return isRegularFile(path) && path.toString().endsWith(".jar", true)
}

/**
 * Return true only if path is a jar or zip file.
 *
 * @param path to a file/dir specified as a string
 * @return true if file ending in `.zip` or `.jar`
 */
fun isZipOrJarFile(path: String): Boolean {
  return isZipFile(path) || isJarFile(path)
}


/**
 * Return true only if path is a jar or zip file.
 *
 * @param path to a file/dir specified as a [Path]
 * @return true if file ending in `.zip` or `.jar`
 */
fun isZipOrJarFile(path: Path): Boolean {
  return isZipFile(path) || isJarFile(path)
}

fun isRegularFile(path: String): Boolean {
  return FileSystem.SYSTEM.metadata(path.toPath()).isRegularFile
}

fun isRegularFile(path: Path): Boolean {
  return FileSystem.SYSTEM.metadata(path).isRegularFile
}

fun newInputStream(path: String): InputStream {
  return FileSystem.SYSTEM.source(path.toPath()).buffer().inputStream()
}

fun newInputStream(path: Path): InputStream {
  return FileSystem.SYSTEM.source(path).buffer().inputStream()
}

fun expandIfZip(zipFile: String, destination: String): Path {
  if (!isZipFile(zipFile)) {
    return zipFile.toPath()
  }
  val destDir = destination.toPath()
  val buffer = ByteArray(1024)
  var zis: ZipInputStream? = null
  try {
    zis = ZipInputStream(FileInputStream(zipFile))
    var ze: ZipEntry? = zis.nextEntry
    while (ze != null) {
      val newFile = destDir.resolve(ze.name)
      if (ze.isDirectory) {
        FileSystem.SYSTEM.createDirectories(newFile)
      } else {
        val parent = newFile.parent
        if (parent != null) {
          FileSystem.SYSTEM.createDirectories(parent)
        }
        val fos = FileOutputStream(newFile.toFile())
        var len: Int
        while (zis.read(buffer).also { len = it } > 0) {
          fos.write(buffer, 0, len)
        }
        fos.close()
      }
      zis.closeEntry()
      ze = zis.nextEntry
    }
    zis.closeEntry()
  } catch (e: IOException) {
    e.printStackTrace()
  } finally {
    try {
      zis?.close()
    } catch (e: IOException) {
      e.printStackTrace()
    }
  }
  return destDir
}

fun expandIfZip(zipFile: Path, destination: Path): Path {
  if (!isZipFile(zipFile)) {
    return zipFile
  }
  val buffer = ByteArray(1024)
  var zis: ZipInputStream? = null
  try {
    zis = ZipInputStream(FileInputStream(zipFile.toFile()))
    var ze: ZipEntry? = zis.nextEntry
    while (ze != null) {
      val newFile = destination.resolve(ze.name)
      if (ze.isDirectory) {
        FileSystem.SYSTEM.createDirectories(newFile)
      } else {
        val parent = newFile.parent
        if (parent != null) {
          FileSystem.SYSTEM.createDirectories(parent)
        }
        val fos = FileOutputStream(newFile.toFile())
        var len: Int
        while (zis.read(buffer).also { len = it } > 0) {
          fos.write(buffer, 0, len)
        }
        fos.close()
      }
      zis.closeEntry()
      ze = zis.nextEntry
    }
    zis.closeEntry()
  } catch (e: IOException) {
    e.printStackTrace()
  } finally {
    try {
      zis?.close()
    } catch (e: IOException) {
      e.printStackTrace()
    }
  }
  return destination
}

fun expandIfZipUnit(zipFile: String, destination: String) {
  if (!isZipFile(zipFile)) {
    return
  }
  val destDir = File(destination)
  if (!destDir.exists()) {
    destDir.mkdir()
  }
  val zipIn = ZipInputStream(FileInputStream(zipFile))
  var entry: ZipEntry? = zipIn.nextEntry
  // iterates over entries in the zip file
  while (entry != null) {
    val filePath = destination + File.separator + entry.name
    if (!entry.isDirectory) {
      // if the entry is a file, extracts it
      extractFile(zipIn, filePath)
    } else {
      // if the entry is a directory, make the directory
      val dir = File(filePath)
      dir.mkdir()
    }
    zipIn.closeEntry()
    entry = zipIn.nextEntry
  }
  zipIn.close()
}

fun expandIfZipUnit(zipFile: Path, destination: Path) {
  if (!isZipFile(zipFile)) {
    return
  }
  val destDir = destination.toFile()
  if (!destDir.exists()) {
    destDir.mkdir()
  }
  val zipIn = ZipInputStream(FileInputStream(zipFile.toFile()))
  var entry: ZipEntry? = zipIn.nextEntry
  // iterates over entries in the zip file
  while (entry != null) {
    val filePath = destination.resolve(entry.name)
    if (!entry.isDirectory) {
      // if the entry is a file, extracts it
      extractFile(zipIn, filePath)
    } else {
      // if the entry is a directory, make the directory
      val dir = filePath.toFile()
      dir.mkdir()
    }
    zipIn.closeEntry()
    entry = zipIn.nextEntry
  }
  zipIn.close()
}

fun extractFile(zipIn: ZipInputStream, filePath: String) {
  val bos = BufferedOutputStream(FileOutputStream(filePath))
  val bytesIn = ByteArray(4096)
  var read: Int
  while (zipIn.read(bytesIn).also { read = it } != -1) {
    bos.write(bytesIn, 0, read)
  }
  bos.close()
}

fun extractFile(zipIn: ZipInputStream, filePath: Path) {
  val bos = BufferedOutputStream(FileOutputStream(filePath.toFile()))
  val bytesIn = ByteArray(4096)
  var read: Int
  while (zipIn.read(bytesIn).also { read = it } != -1) {
    bos.write(bytesIn, 0, read)
  }
  bos.close()
}

fun Path.toUri(): URI {
  return this.toFile().toURI()
}

fun Path.isZip(): Boolean {
  return isZipFile(this)
}
