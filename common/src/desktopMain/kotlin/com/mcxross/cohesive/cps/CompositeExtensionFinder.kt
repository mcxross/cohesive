package com.mcxross.cohesive.cps

import com.mcxross.cohesive.common.frontend.openapi.ui.view.CohesiveView

/**
 * The default implementation for [ExtensionFinder].
 *
 * It's a compound `ExtensionFinder` that serves as a container for other [ExtensionFinder]s.
 * The finder methods will loop through all the [ExtensionFinder]s trying to find the applicable and if found,
 * call that and return the result.
 */
open class CompositeExtensionFinder(val pluginManager: PluginManager) : ExtensionFinder,
    PluginStateListener {

    /**
     * The cache list of [ExtensionFinder]s.
     *
     * By default, it contains the [LegacyExtensionFinder].
     */
    private var finders: MutableList<ExtensionFinder> = mutableListOf(LegacyExtensionFinder(pluginManager))

    /**
     * Add a [ExtensionFinder] to the container.
     *
     * @since 0.1.0
     */
    fun add(finder: ExtensionFinder) {
        finders.add(finder)
    }

    override fun find(): CohesiveView? {
        var cohesiveView: CohesiveView? = null
        finders.forEach {
            cohesiveView = it.find()
        }
        return cohesiveView
    }

    /**
     * Find a list of [ExtensionWrapper]s for the given [com.mcxross.cohesive.csp.annotation.Extension] class.
     *
     * @param type the [com.mcxross.cohesive.csp.annotation.Extension] class
     * @return a list of [ExtensionWrapper]s
     * @since 0.1.0
     */
    override fun <T> find(type: Class<T>): List<ExtensionWrapper<T>> {
        val extensions: MutableList<ExtensionWrapper<T>> = ArrayList()
        finders.forEach {
            extensions.addAll(it.find(type))
        }
        return extensions
    }

    /**
     * Find a list of [ExtensionWrapper]s for the given [com.mcxross.cohesive.csp.annotation.Extension] class and [Plugin] id.
     *
     * @param type the [com.mcxross.cohesive.csp.annotation.Extension] class
     * @param pluginId the [Plugin] id
     * @return a list of [ExtensionWrapper]s
     * @since 0.1.0
     */
    override fun <T> find(type: Class<T>, pluginId: String): List<ExtensionWrapper<T>> {
        val extensions: MutableList<ExtensionWrapper<T>> = ArrayList()
        finders.forEach {
            extensions.addAll(it.find(type, pluginId))
        }
        return extensions
    }

    /**
     * Find a list of [ExtensionWrapper]s for the given [Plugin] id.
     *
     * @param pluginId the [Plugin] id
     * @return a list of [ExtensionWrapper]s
     * @since 0.1.0
     */
    override fun <T> find(pluginId: String): List<ExtensionWrapper<T>> {
        val extensions: MutableList<ExtensionWrapper<T>> = ArrayList()
        finders.forEach {
            extensions.addAll(it.find(pluginId))
        }
        return extensions
    }

    /**
     * Find a list of Extension class names for the given plugin id.
     *
     * @param pluginId the plugin id
     * @return a list of Extension class names
     * @since 0.1.0
     */
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

}