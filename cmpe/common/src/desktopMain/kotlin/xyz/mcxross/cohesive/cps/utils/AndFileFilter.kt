package xyz.mcxross.cohesive.cps.utils

import java.io.File
import java.io.FileFilter

/**
 * This filter providing conditional AND logic across a list of file filters. This filter returns
 * `true` if all filters in the list return `true`. Otherwise, it returns `false`. Checking of the
 * file filter list stops when the first filter returns `false`.
 */
class AndFileFilter @JvmOverloads constructor(val fileFilters: MutableList<FileFilter> = ArrayList()) :
  FileFilter {

  constructor(vararg fileFilters: FileFilter) : this(mutableListOf<FileFilter>(*fileFilters))

  fun removeFileFilter(fileFilter: FileFilter): Boolean {
    return fileFilters.remove(fileFilter)
  }

  override fun accept(file: File): Boolean {
    if (fileFilters.isEmpty()) {
      return false
    }
    fileFilters.forEach {
      if (!it.accept(file)) {
        return false
      }
    }
    return true
  }
}
