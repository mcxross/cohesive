package com.mcxross.cohesive.cps

/**
 * A wrapper over extension instance.
 */
class ExtensionWrapper<T>(descriptor: ExtensionDescriptor, extensionFactory: ExtensionFactory) :
    Comparable<ExtensionWrapper<T>?> {
    val descriptor: ExtensionDescriptor
    private val extensionFactory: ExtensionFactory
    var extension: T? = null // cache
        get() {
            if (field == null) {
                field = extensionFactory.create(descriptor.extensionClass) as T?
            }
            return field
        }
        private set

    init {
        this.descriptor = descriptor
        this.extensionFactory = extensionFactory
    }


    val ordinal: Int
        get() = descriptor.ordinal

    override operator fun compareTo(other: ExtensionWrapper<T>?): Int {
        return ordinal - other!!.ordinal
    }
}