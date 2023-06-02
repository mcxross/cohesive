package xyz.mcxross.cohesive.cps.utils

import java.io.File
import java.io.FileFilter

/**
 * Filter accepts files that are directories.
 */
class DirectoryFileFilter : FileFilter {
  override fun accept(file: File): Boolean {
    return file.isDirectory
  }
}
