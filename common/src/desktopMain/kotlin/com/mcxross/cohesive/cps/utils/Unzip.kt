package com.mcxross.cohesive.cps.utils

import com.mcxross.cohesive.common.frontend.utils.deleteRecursively
import com.mcxross.cohesive.common.utils.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

/**
 * This class extracts the content of the plugin zip into a directory.
 * It's a class for only the internal use.
 */
class Unzip(
    /**
     * Holds path to zip file.
     */
    var source: File,
    /**
     * Holds the destination directory.
     * File will be unzipped into the destination directory.
     */
    var destination: File
) {


    /**
     * Extract the content of zip file (`source`) to destination directory.
     * If destination directory already exists it will be deleted before.
     */
    @Throws(IOException::class)
    fun extract() {
        Log.d {
            "Extract content of $source to $destination "
        }

        // delete destination directory if exists
        if (destination.exists() && destination.isDirectory) {
            deleteRecursively(destination.absolutePath)
        }
        ZipInputStream(FileInputStream(source)).use { zipInputStream ->
            var zipEntry: ZipEntry
            while (zipInputStream.nextEntry.also { zipEntry = it } != null) {
                val file = File(destination, zipEntry.name)

                // create intermediary directories - sometimes zip don't plus them
                val dir = File(file.parent)
                mkdirsOrThrow(dir)
                if (zipEntry.isDirectory) {
                    mkdirsOrThrow(file)
                } else {
                    val buffer = ByteArray(1024)
                    var length: Int
                    FileOutputStream(file).use { fos ->
                        while (zipInputStream.read(buffer).also { length = it } >= 0) {
                            fos.write(buffer, 0, length)
                        }
                    }
                }
            }
        }
    }

    companion object {
        @Throws(IOException::class)
        private fun mkdirsOrThrow(dir: File) {
            if (!dir.exists() && !dir.mkdirs()) {
                throw IOException("Failed to create directory $dir")
            }
        }
    }
}
