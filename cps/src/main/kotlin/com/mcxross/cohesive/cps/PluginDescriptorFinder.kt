package com.mcxross.cohesive.cps

import java.nio.file.Path

/**
 * Find a plugin descriptor for a plugin path.
 * You can find the plugin descriptor in manifest file [ManifestPluginDescriptorFinder],
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