package com.mcxross.cohesive.cps.utils

import java.io.File
import java.io.FileFilter
import java.util.*


/**
 * This filter providing conditional AND logic across a list of file filters.
 * This filter returns `true` if all filters in the list return `true`. Otherwise, it returns `false`.
 * Checking of the file filter list stops when the first filter returns `false`.
 *
 */
class AndFileFilter @JvmOverloads constructor(fileFilters: List<FileFilter> = ArrayList()) : FileFilter {

    /** The list of file filters.  */
     var fileFilters: MutableList<FileFilter>
        get() {
            return Collections.unmodifiableList(field)
        }

    init {
        this.fileFilters = ArrayList(fileFilters)
    }

    constructor(vararg fileFilters: FileFilter) : this(listOf<FileFilter>(*fileFilters)) {}

    fun addFileFilter(fileFilter: FileFilter): AndFileFilter {
        fileFilters.add(fileFilter)
        return this
    }

    fun removeFileFilter(fileFilter: FileFilter): Boolean {
        return fileFilters.remove(fileFilter)
    }

    override fun accept(file: File): Boolean {
        if (fileFilters.isEmpty()) {
            return false
        }
        for (fileFilter in fileFilters) {
            if (!fileFilter.accept(file)) {
                return false
            }
        }
        return true
    }
}