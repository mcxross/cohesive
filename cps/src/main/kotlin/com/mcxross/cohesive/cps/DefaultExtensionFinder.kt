package com.mcxross.cohesive.cps

/**
 * The default implementation for [ExtensionFinder].
 * It's a compound `ExtensionFinder`.
 */
class DefaultExtensionFinder(pluginManager: PluginManager) : ExtensionFinder,
    PluginStateListener {
    protected var pluginManager: PluginManager
    protected var finders: MutableList<ExtensionFinder> = ArrayList()

    init {
        this.pluginManager = pluginManager
        add(LegacyExtensionFinder(pluginManager))
        //        add(new ServiceProviderExtensionFinder(pluginManager));
    }

    override fun <T> find(type: Class<T>): List<ExtensionWrapper<T>> {
        val extensions: MutableList<ExtensionWrapper<T>> = ArrayList()
        for (finder in finders) {
            extensions.plus(finder.find(type))
        }
        return extensions
    }

    override fun <T> find(type: Class<T>, pluginId: String): List<ExtensionWrapper<T>> {
        val extensions: MutableList<ExtensionWrapper<T>> = ArrayList()
        for (finder in finders) {
            extensions.plus(finder.find(type, pluginId))
        }
        return extensions
    }

    override fun <T> find(pluginId: String): List<ExtensionWrapper<T>> {
        val extensions: MutableList<ExtensionWrapper<T>> = ArrayList()
        for (finder in finders) {
            extensions.plus(finder.find(pluginId))
        }
        return extensions
    }

    override fun findClassNames(pluginId: String): Set<String> {
        val classNames: MutableSet<String> = HashSet()
        for (finder in finders) {
            classNames.addAll(finder.findClassNames(pluginId))
        }
        return classNames
    }

    override fun pluginStateChanged(event: PluginStateEvent) {
        for (finder in finders) {
            if (finder is PluginStateListener) {
                (finder as PluginStateListener).pluginStateChanged(event)
            }
        }
    }

    fun addServiceProviderExtensionFinder(): DefaultExtensionFinder {
        return add(ServiceProviderExtensionFinder(pluginManager))
    }

    fun add(finder: ExtensionFinder): DefaultExtensionFinder {
        finders.add(finder)
        return this
    }
}