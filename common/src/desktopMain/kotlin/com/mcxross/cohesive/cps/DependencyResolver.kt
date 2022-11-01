package com.mcxross.cohesive.cps

import com.mcxross.cohesive.common.utils.Log
import com.mcxross.cohesive.cps.DependencyResolver.Result
import com.mcxross.cohesive.cps.utils.DirectedGraph

/**
 * This class builds a dependency graph for a list of plugins (descriptors).
 * The entry point is the [.resolve] method, method that returns a [Result] object.
 * The `Result` class contains nice information about the result of resolve operation (if it's a cyclic dependency,
 * they are not found dependencies, they are dependencies with wrong version).
 * This class is very useful for if-else scenarios.
 *
 * Only some attributes (pluginId, dependencies and pluginVersion) from [PluginDescriptor] are used in
 * the process of `resolve` operation.
 */
class DependencyResolver(val versionManager: VersionManager) {
  // the value is 'pluginId'
  private var dependenciesGraph: DirectedGraph<String>? = null

  // the value is 'pluginId'
  private var dependentsGraph: DirectedGraph<String>? = null
  private var resolved = false

  fun resolve(plugins: List<PluginDescriptor>): Result {
    // create graphs
    dependenciesGraph = DirectedGraph()
    dependentsGraph = DirectedGraph()

    // populate graphs
    val pluginByIds: MutableMap<String, PluginDescriptor> = HashMap()
    plugins.forEach {
      addPlugin(it)
      pluginByIds[it.pluginId] = it
    }
    Log.d { "Graph: $dependenciesGraph" }

    // get a sorted list of dependencies
    val sortedPlugins: List<String> = dependenciesGraph!!.reverseTopologicalSort()
    Log.d { "Plugins order: $sortedPlugins" }

    // create the result object
    val result = Result(sortedPlugins)
    resolved = true
    sortedPlugins.forEach { pluginId ->
      if (!pluginByIds.containsKey(pluginId)) {
        pluginId.let { result.notFoundDependencies.add(it) }
      }
    }

    // check dependencies versions
    for (plugin: PluginDescriptor in plugins) {
      val pluginId: String = plugin.pluginId
      val existingVersion: String = plugin.version
      val dependents = getDependents(pluginId)
      while (dependents.isNotEmpty()) {
        val dependentId: String = dependents.removeAt(0)
        val dependent: PluginDescriptor? = pluginByIds[dependentId]
        val requiredVersion = getDependencyVersionSupport(dependent, pluginId)
        val ok = checkDependencyVersion(requiredVersion, existingVersion)
        if (!ok) {
          result.wrongVersionDependencies.add(
            WrongDependencyVersion(
              pluginId,
              dependentId,
              existingVersion,
              requiredVersion,
            ),
          )
        }
      }
    }
    return result
  }

  /**
   * Retrieves the plugins ids that the given corePlugin id directly depends on.
   *
   * @param pluginId the unique corePlugin identifier, specified in its metadata
   * @return an immutable list of dependencies (new list for each call)
   */
  fun getDependencies(pluginId: String): List<String> {
    checkResolved()
    return ArrayList<String>(dependenciesGraph!!.getNeighbors(pluginId))
  }

  /**
   * Retrieves the plugins ids that the given content is a direct dependency of.
   *
   * @param pluginId the unique corePlugin identifier, specified in its metadata
   * @return an immutable list of dependents (new list for each call)
   */
  fun getDependents(pluginId: String): MutableList<String> {
    checkResolved()
    return ArrayList(dependentsGraph!!.getNeighbors(pluginId))
  }

  /**
   * Check if an existing version of dependency is compatible with the required version (from corePlugin descriptor).
   *
   * @param requiredVersion
   * @param existingVersion
   * @return
   */
  protected fun checkDependencyVersion(requiredVersion: String, existingVersion: String): Boolean {
    return versionManager.checkVersionConstraint(existingVersion, requiredVersion)
  }

  private fun addPlugin(descriptor: PluginDescriptor) {
    val pluginId: String = descriptor.pluginId
    val dependencies: List<PluginDependency>? = descriptor.dependencies
    if (dependencies!!.isEmpty()) {
      dependenciesGraph!!.addVertex(pluginId)
      dependentsGraph!!.addVertex(pluginId)
    } else {
      var edgeAdded = false
      for (dependency: PluginDependency in dependencies) {
        // Don't register optional plugins in the dependency graph to avoid automatic disabling of the corePlugin,
        // if an optional dependency is missing.
        if (!dependency.isOptional) {
          edgeAdded = true
          dependenciesGraph?.addEdge(pluginId, dependency.isOptional.toString())
          dependency.pluginId?.let { dependentsGraph!!.addEdge(it, pluginId) }
        }
      }

      // Register the corePlugin without dependencies, if all of its dependencies are optional.
      if (!edgeAdded) {
        dependenciesGraph?.addVertex(pluginId)
        dependentsGraph?.addVertex(pluginId)
      }
    }
  }

  private fun checkResolved() {
    if (!resolved) {
      throw IllegalStateException("Call 'resolve' method first")
    }
  }

  private fun getDependencyVersionSupport(
    dependent: PluginDescriptor?,
    dependencyId: String
  ): String {
    val dependencies: List<PluginDependency>? = dependent!!.dependencies
    for (dependency: PluginDependency in dependencies!!) {
      if (dependencyId == dependency.pluginId) {
        return dependency.pluginVersionSupport
      }
    }
    throw IllegalStateException(
      "Cannot find a dependency with id '" + dependencyId +
        "' for corePlugin '" + dependent.pluginId + "'",
    )
  }

  class Result internal constructor(val sortedPlugins: List<String>) {
    private var cyclicDependency = false

    // value is "pluginId"
    val notFoundDependencies: MutableList<String> = ArrayList()

    val wrongVersionDependencies: MutableList<WrongDependencyVersion> = ArrayList()

    /**
     * Returns true is a cyclic dependency was detected.
     */
    fun hasCyclicDependency(): Boolean {
      return cyclicDependency
    }

  }

  class WrongDependencyVersion internal constructor(
    val dependencyId: String, // value is "pluginId"
    var dependentId: String,
    val existingVersion: String,
    val requiredVersion: String,
  ) {

    override fun toString(): String {
      return ("WrongDependencyVersion{" +
        "dependencyId='" + dependencyId + '\'' +
        ", dependentId='" + dependentId + '\'' +
        ", existingVersion='" + existingVersion + '\'' +
        ", requiredVersion='" + requiredVersion + '\'' +
        '}')
    }
  }

  /**
   * It will be thrown if a cyclic dependency is detected.
   */
  class CyclicDependencyException : PluginRuntimeException("Cyclic dependencies")

  /**
   * Indicates that the dependencies required were not found.
   */
  class DependenciesNotFoundException(val dependencies: List<String>) :
    PluginRuntimeException("Dependencies '{}' not found", dependencies)

  /**
   * Indicates that some dependencies have wrong version.
   */
  class DependenciesWrongVersionException(val dependencies: List<WrongDependencyVersion>) :
    PluginRuntimeException("Dependencies '{}' have wrong version", dependencies)

}
