package com.mcxross.cohesive.cps

import com.mcxross.cohesive.common.frontend.api.ui.view.CohesiveView

interface ExtensionFinder {

  fun find(): CohesiveView?

  /** Retrieves a list with all extensions found for an extension point. */
  fun <T> find(type: Class<T>): List<ExtensionWrapper<T>>

  /** Retrieves a list with all extensions found for an extension point and a corePlugin. */
  fun <T> find(type: Class<T>, pluginId: String): List<ExtensionWrapper<T>>

  /** Retrieves a list with all extensions found for a corePlugin */
  fun <T> find(pluginId: String): List<ExtensionWrapper<T>>

  /** Retrieves a list with all extension class names found for a corePlugin. */
  fun findClassNames(pluginId: String): Set<String>
}
