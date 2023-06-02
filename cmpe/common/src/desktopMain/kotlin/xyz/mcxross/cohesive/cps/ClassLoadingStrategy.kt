package xyz.mcxross.cohesive.cps

/**
 * [ClassLoadingStrategy] will be used to configure [PluginClassLoader] loading order and contains
 * all possible options supported by [PluginClassLoader] where: `A = Application Source (load
 * classes from parent classLoader) P = Plugin Source (load classes from this classloader) D =
 * Dependencies (load classes from dependencies) ` *
 */
class ClassLoadingStrategy(val sources: List<Source>) {

  enum class Source {
    PLUGIN,
    APPLICATION,
    DEPENDENCIES
  }

  companion object {
    /** application(parent) -> Plugin -> dependencies */
    val APD = ClassLoadingStrategy(listOf(Source.APPLICATION, Source.PLUGIN, Source.DEPENDENCIES))

    /** application(parent) -> dependencies -> Plugin */
    val ADP = ClassLoadingStrategy(listOf(Source.APPLICATION, Source.DEPENDENCIES, Source.PLUGIN))

    /** Plugin -> application(parent) -> dependencies */
    val PAD = ClassLoadingStrategy(listOf(Source.PLUGIN, Source.APPLICATION, Source.DEPENDENCIES))

    /** dependencies -> application(parent) -> Plugin */
    val DAP = ClassLoadingStrategy(listOf(Source.DEPENDENCIES, Source.APPLICATION, Source.PLUGIN))

    /** dependencies -> Plugin -> application(parent) */
    val DPA = ClassLoadingStrategy(listOf(Source.DEPENDENCIES, Source.PLUGIN, Source.APPLICATION))

    /** Plugin -> dependencies -> application(parent) */
    val PDA = ClassLoadingStrategy(listOf(Source.PLUGIN, Source.DEPENDENCIES, Source.APPLICATION))
  }
}
