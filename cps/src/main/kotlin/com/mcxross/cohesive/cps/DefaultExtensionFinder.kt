package com.mcxross.cohesive.cps

/**
 * The default implementation for [ExtensionFinder].
 * It's a compound `ExtensionFinder`.
 */
open class DefaultExtensionFinder(val pluginManager: PluginManager) : ExtensionFinder,
    PluginStateListener {

    private var finders: MutableList<ExtensionFinder> = mutableListOf(LegacyExtensionFinder(pluginManager))

    override fun <T> find(type: Class<T>): List<ExtensionWrapper<T>> {
        val extensions: MutableList<ExtensionWrapper<T>> = ArrayList()
        finders.forEach {
            extensions.addAll(it.find(type))
        }
        return extensions
    }

    override fun <T> find(type: Class<T>, pluginId: String): List<ExtensionWrapper<T>> {
        val extensions: MutableList<ExtensionWrapper<T>> = ArrayList()
        finders.forEach {
            extensions.addAll(it.find(type, pluginId))
        }
        return extensions
    }

    override fun <T> find(pluginId: String): List<ExtensionWrapper<T>> {
        val extensions: MutableList<ExtensionWrapper<T>> = ArrayList()
        finders.forEach {
            extensions.addAll(it.find(pluginId))
        }
        return extensions
    }

    override fun findClassNames(pluginId: String): Set<String> {
        val classNames: MutableSet<String> = HashSet()
        finders.forEach {
            classNames.addAll(it.findClassNames(pluginId))
        }
        return classNames
    }

    override fun pluginStateChanged(event: PluginStateEvent) {
        finders.forEach {
            if (it is PluginStateListener) {
                (it as PluginStateListener).pluginStateChanged(event)
            }
        }
    }

    fun addServiceProviderExtensionFinder() {
        finders.add(ServiceProviderExtensionFinder(pluginManager))
    }

    fun add(finder: ExtensionFinder) {
        finders.add(finder)
    }
}