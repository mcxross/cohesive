package com.mcxross.cohesive.cps.utils

import java.io.File
import java.io.FileFilter

/**
 * Filter accepts any file with this name. The case of the filename is ignored.
 */
class NameFileFilter(private val name: String) : FileFilter {
  override fun accept(file: File): Boolean {
    // perform a case-insensitive check.
    return file.name.equals(name, ignoreCase = true)
  }
}
