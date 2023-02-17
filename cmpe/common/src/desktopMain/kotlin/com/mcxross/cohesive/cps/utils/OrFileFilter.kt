package com.mcxross.cohesive.cps.utils

import java.io.File
import java.io.FileFilter

/**
 * This filter providing conditional OR logic across a list of file filters.
 * This filter returns `true` if one filter in the list return `true`. Otherwise, it returns `false`.
 * Checking of the file filter list stops when the first filter returns `true`.
 */
class OrFileFilter @JvmOverloads constructor(val fileFilters: MutableList<FileFilter> = ArrayList()) :
  FileFilter {
  constructor(vararg fileFilters: FileFilter) : this(mutableListOf<FileFilter>(*fileFilters))

  override fun accept(file: File): Boolean {
    if (fileFilters.isEmpty()) {
      return true
    }
    fileFilters.forEach {
      if (it.accept(file)) {
        return true
      }
    }
    return false
  }
}
