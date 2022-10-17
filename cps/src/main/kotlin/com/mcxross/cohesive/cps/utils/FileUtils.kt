package com.mcxross.cohesive.cps.utils

import okio.Path.Companion.toPath
import okio.buffer
import java.io.*
import java.net.URI
import java.nio.charset.StandardCharsets
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.util.*

object FileUtils {
    @Throws(IOException::class)
    fun readLines(path: Path, ignoreComments: Boolean): List<String> {
        val file = path.toFile()
        if (!file.isFile) {
            return ArrayList()
        }
        val lines: MutableList<String> = ArrayList()
        BufferedReader(FileReader(file)).use { reader ->
            var line: String
            while (reader.readLine().also { line = it } != null) {
                if (ignoreComments && !line.startsWith("#") && !lines.contains(line)) {
                    lines.add(line)
                }
            }
        }
        return lines
    }

    fun readString(path: String) : String {
        val file = okio.FileSystem.SYSTEM.source(path.toPath())
        return file.buffer().readUtf8()
    }

    /**
     * Use [.writeLines] instead.
     */
    @Deprecated("")
    @Throws(IOException::class)
    fun writeLines(lines: Collection<String?>?, file: File) {
        writeLines(lines, file.toPath())
    }

    @Throws(IOException::class)
    fun writeLines(lines: Collection<String?>?, path: Path?) {
        Files.write(path, lines, StandardCharsets.UTF_8)
    }

    /**
     * Delete a file or recursively delete a folder, do not follow symlinks.
     *
     * @param path the file or folder to delete
     * @throws IOException if something goes wrong
     */
    @Throws(IOException::class)
    fun delete(path: Path) {
        Files.walkFileTree(path, object : SimpleFileVisitor<Path?>() {
            @Throws(IOException::class)
            override fun visitFile(file: Path?, attrs: BasicFileAttributes?): FileVisitResult? {
                if (!attrs!!.isSymbolicLink) {
                    path.let { Files.delete(it) }
                }
                return FileVisitResult.CONTINUE
            }

            @Throws(IOException::class)
            override fun postVisitDirectory(dir: Path?, exc: IOException?): FileVisitResult? {
                dir?.let { Files.delete(it) }
                return FileVisitResult.CONTINUE
            }
        })
    }

    fun getJars(folder: Path): List<File> {
        val bucket: MutableList<File> = ArrayList()
        getJars(bucket, folder)
        return bucket
    }

    private fun getJars(bucket: MutableList<File>, folder: Path) {
        val jarFilter: FileFilter = JarFileFilter()
        val directoryFilter: FileFilter = DirectoryFileFilter()
        if (Files.isDirectory(folder)) {
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
                getJars(bucket, directory.toPath())
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
            val newPath = basePath.resolveSibling(basePath.fileName.toString() + ending)
            if (Files.exists(newPath)) {
                return newPath
            }
        }
        return null
    }

    /**
     * Delete a file (not recursively) and ignore any errors.
     *
     * @param path the path to delete
     */
    fun optimisticDelete(path: Path?) {
        if (path == null) {
            return
        }
        try {
            Files.delete(path)
        } catch (ignored: IOException) {
            // ignored
        }
    }

    /**
     * Unzip a zip file in a directory that has the same name as the zip file.
     * For example if the zip file is `my-plugin.zip` then the resulted directory
     * is `my-plugin`.
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
        val pluginZipDate = Files.getLastModifiedTime(filePath)
        val fileName = filePath.fileName.toString()
        val directoryName = fileName.substring(0, fileName.lastIndexOf("."))
        val pluginDirectory = filePath.resolveSibling(directoryName)
        if (!Files.exists(pluginDirectory) || pluginZipDate > Files.getLastModifiedTime(pluginDirectory)) {
            // expand '.zip' file
            val unzip = Unzip(source = filePath.toFile(), destination = pluginDirectory.toFile())
            unzip.extract()
            Log.i { "Expanded plugin zip ${filePath.fileName} in ${pluginDirectory.fileName}" }
        }
        return pluginDirectory
    }

    /**
     * Return true only if path is a zip file.
     *
     * @param path to a file/dir
     * @return true if file with `.zip` ending
     */
    fun isZipFile(path: Path): Boolean {
        return Files.isRegularFile(path) && path.toString().lowercase(Locale.getDefault()).endsWith(".zip")
    }

    /**
     * Return true only if path is a jar file.
     *
     * @param path to a file/dir
     * @return true if file with `.jar` ending
     */
    fun isJarFile(path: Path): Boolean {
        return Files.isRegularFile(path) && path.toString().lowercase(Locale.getDefault()).endsWith(".jar")
    }

    /**
     * Return true only if path is a jar or zip file.
     *
     * @param path to a file/dir
     * @return true if file ending in `.zip` or `.jar`
     */
    fun isZipOrJarFile(path: Path): Boolean {
        return isZipFile(path) || isJarFile(path)
    }

    @Throws(IOException::class)
    fun getPath(path: Path, first: String, vararg more: String?): Path {
        var uri = path.toUri()
        if (isZipOrJarFile(path)) {
            var pathString = path.toAbsolutePath().toString()
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
        return getFileSystem(uri).getPath(first, *more)
    }

    fun closePath(path: Path?) {
        if (path != null) {
            try {
                path.fileSystem.close()
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
                        return file.toPath()
                    }
                } else if (file.isDirectory) {
                    val foundFile = findFile(file.toPath(), fileName)
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