package com.mcxross.cohesive.cps.utils

import java.io.File
import java.io.FileFilter
import java.util.*

/**
 * Filter accepts any file ending in extension. The case of the filename is ignored.
 */
open class ExtensionFileFilter(private val extension: String) : FileFilter {
    override fun accept(file: File): Boolean {
        // perform a case-insensitive check.
        return file.name.uppercase(Locale.getDefault()).endsWith(extension.uppercase(Locale.getDefault()))
    }
}