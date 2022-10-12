package com.mcxross.cohesive.cps

interface ExtensionFinder {
    /**
     * Retrieves a list with all extensions found for an extension point.
     */
    fun <T> find(type: Class<T>): List<ExtensionWrapper<T>>

    /**
     * Retrieves a list with all extensions found for an extension point and a plugin.
     */
    fun <T> find(type: Class<T>, pluginId: String): List<ExtensionWrapper<T>>

    /**
     * Retrieves a list with all extensions found for a plugin
     */
    fun <T> find(pluginId: String): List<ExtensionWrapper<T>>

    /**
     * Retrieves a list with all extension class names found for a plugin.
     */
    fun findClassNames(pluginId: String): Set<String>
}
