package com.mcxross.cohesive.cps.asm

import com.mcxross.cohesive.common.utils.Log
import org.objectweb.asm.ClassReader
import java.io.IOException
import java.util.*

/**
 * This class holds the parameters of an [com.mcxross.cohesive.csp.annotation.Extension]
 * annotation defined for a certain class.
 */
class ExtensionInfo private constructor(
    /**
     * Get the name of the class, for which extension i was created.
     *
     * @return absolute class name
     */
    val className: String,
) {

    /**
     * Get the [Extension.ordinal] value, that was assigned to the extension.
     *
     * @return ordinal value
     */
    var ordinal = 0

    /**
     * Get the [Extension.plugins] value, that was assigned to the extension.
     *
     * @return ordinal value
     */
    var plugins: List<String> = ArrayList()
        get() {
            return Collections.unmodifiableList(field)
        }

    /**
     * Get the [Extension.points] value, that was assigned to the extension.
     *
     * @return ordinal value
     */
    var points: List<String> = ArrayList()
        get() {
            return Collections.unmodifiableList(field)
        }

    companion object {

        /**
         * Load an [ExtensionInfo] for a certain class.
         *
         * @param className absolute class name
         * @param classLoader class pluginLoader to access the class
         * @return the [ExtensionInfo], if the class was annotated with an [Extension], otherwise null
         */
        fun load(
            className: String,
            classLoader: ClassLoader,
        ): ExtensionInfo? {
            try {
                classLoader.getResourceAsStream(className.replace('.', '/') + ".class").use { input ->
                    val info = ExtensionInfo(className)
                    ClassReader(input)
                        .accept(ExtensionVisitor(info), ClassReader.SKIP_DEBUG)
                    return info
                }
            } catch (e: IOException) {
                Log.e { e.message.toString() }
                return null
            }
        }
    }
}