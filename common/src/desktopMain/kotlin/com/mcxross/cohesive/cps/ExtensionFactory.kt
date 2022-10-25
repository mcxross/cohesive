package com.mcxross.cohesive.cps

/**
 * Creates an extension instance.
 */
interface ExtensionFactory {
    fun <T> create(extensionClass: Class<T>): T
}
