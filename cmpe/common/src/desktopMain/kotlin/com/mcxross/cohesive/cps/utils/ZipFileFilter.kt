package com.mcxross.cohesive.cps.utils


/**
 * The extension that this filter will search for.
 */
private const val ZIP_EXTENSION = ".ZIP"

/**
 * File filter that accepts all files ending with .ZIP.
 * This filter is case-insensitive.
 */
object ZipFileFilter : ExtensionFileFilter(ZIP_EXTENSION)
