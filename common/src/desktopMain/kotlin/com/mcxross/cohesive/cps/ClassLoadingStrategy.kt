package com.mcxross.cohesive.cps

/**
 * [ClassLoadingStrategy] will be used to configure [PluginClassLoader] loading order
 * and contains all possible options supported by [PluginClassLoader] where:
 * `A = Application Source (load classes from parent classLoader)
 * P = CorePlugin Source (load classes from this classloader)
 * D = Dependencies (load classes from dependencies)
` *
 */
class ClassLoadingStrategy(val sources: List<Source>) {

  enum class Source {
    PLUGIN, APPLICATION, DEPENDENCIES
  }

  companion object {
    /**
     * application(parent) -> corePlugin -> dependencies
     */
    val APD = ClassLoadingStrategy(listOf(Source.APPLICATION, Source.PLUGIN, Source.DEPENDENCIES))

    /**
     * application(parent) -> dependencies -> corePlugin
     */
    val ADP = ClassLoadingStrategy(listOf(Source.APPLICATION, Source.DEPENDENCIES, Source.PLUGIN))

    /**
     * corePlugin -> application(parent) -> dependencies
     */
    val PAD = ClassLoadingStrategy(listOf(Source.PLUGIN, Source.APPLICATION, Source.DEPENDENCIES))

    /**
     * dependencies -> application(parent) -> corePlugin
     */
    val DAP = ClassLoadingStrategy(listOf(Source.DEPENDENCIES, Source.APPLICATION, Source.PLUGIN))

    /**
     * dependencies -> corePlugin -> application(parent)
     */
    val DPA = ClassLoadingStrategy(listOf(Source.DEPENDENCIES, Source.PLUGIN, Source.APPLICATION))

    /**
     * corePlugin -> dependencies -> application(parent)
     */
    val PDA = ClassLoadingStrategy(listOf(Source.PLUGIN, Source.DEPENDENCIES, Source.APPLICATION))
  }
}
