package com.mcxross.cohesive.cps

import java.nio.file.Path

/**
 * Find a holder descriptor for a holder path.
 * You can find the holder descriptor in manifest file [ManifestPluginDescriptorFinder],
 * properties file [PropertiesPluginDescriptorFinder], xml file,
 * java services (with [java.util.ServiceLoader]), etc.
 */
interface PluginDescriptorFinder {
    /**
     * Returns true if this finder is applicable to the given [Path].
     */
    fun isApplicable(pluginPath: Path): Boolean
    fun find(pluginPath: Path): PluginDescriptor?
}