package com.mcxross.cohesive.cps.utils

import java.io.File
import java.io.FileFilter
import java.util.*

/**
 * This filter providing conditional OR logic across a list of file filters.
 * This filter returns `true` if one filter in the list return `true`. Otherwise, it returns `false`.
 * Checking of the file filter list stops when the first filter returns `true`.
 */
class OrFileFilter @JvmOverloads constructor(fileFilters: List<FileFilter> = ArrayList()) :
    FileFilter {

    /** The list of file filters.  */
    var fileFilters: MutableList<FileFilter>
        get() {
            return Collections.unmodifiableList(field)
        }

    init {
        this.fileFilters = ArrayList(fileFilters)
    }

    constructor(vararg fileFilters: FileFilter) : this(listOf<FileFilter>(*fileFilters)) {}

    fun addFileFilter(fileFilter: FileFilter): OrFileFilter {
        fileFilters.add(fileFilter)
        return this
    }

    fun removeFileFilter(fileFilter: FileFilter): Boolean {
        return fileFilters.remove(fileFilter)
    }

    override fun accept(file: File): Boolean {
        if (fileFilters.isEmpty()) {
            return true
        }
        for (fileFilter in fileFilters) {
            if (fileFilter.accept(file)) {
                return true
            }
        }
        return false
    }
}