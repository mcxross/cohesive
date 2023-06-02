package xyz.mcxross.cohesive.cps.utils


/**
 * The extension that this filter will search for.
 */
private const val JAR_EXTENSION = ".JAR"

/**
 * File filter that accepts all files ending with .JAR.
 * This filter is case-insensitive.
 */
class JarFileFilter : ExtensionFileFilter(JAR_EXTENSION)
