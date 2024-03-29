package xyz.mcxross.cohesive.cps

import xyz.mcxross.cohesive.ui.api.view.CohesiveView

interface ExtensionFinder {

  fun find(): CohesiveView?

  /** Retrieves a list with all extensions found for an extension point. */
  fun <T> find(type: Class<T>): List<ExtensionWrapper<T>>

  /** Retrieves a list with all extensions found for an extension point and a Plugin. */
  fun <T> find(type: Class<T>, pluginId: String): List<ExtensionWrapper<T>>

  /** Retrieves a list with all extensions found for a Plugin */
  fun <T> find(pluginId: String): List<ExtensionWrapper<T>>

  /** Retrieves a list with all extension class names found for a Plugin. */
  fun findClassNames(pluginId: String): Set<String>
}
