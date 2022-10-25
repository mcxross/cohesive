package com.mcxross.cohesive.cps

/**
 * The default values are [.CLASSES_DIR] and `#LIB_DIR`.
 */
class DefaultPluginClasspath : PluginClasspath() {
    init {
        addClassesDirectories(CLASSES_DIR)
        addJarsDirectories(LIB_DIR)
    }

    companion object {
        const val CLASSES_DIR = "classes"
        const val LIB_DIR = "lib"
    }
}