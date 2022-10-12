package com.mcxross.cohesive.cps.utils

import java.io.File
import java.io.FileFilter

/**
 * This filter produces a logical NOT of the filters specified.
 */
class NotFileFilter(private val filter: FileFilter) : FileFilter {
    override fun accept(file: File): Boolean {
        return !filter.accept(file)
    }
}