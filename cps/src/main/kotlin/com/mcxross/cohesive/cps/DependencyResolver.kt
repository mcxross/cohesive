package com.mcxross.cohesive.cps

import com.mcxross.cohesive.cps.DependencyResolver.Result
import com.mcxross.cohesive.cps.utils.DirectedGraph
import com.mcxross.cohesive.cps.utils.Log
import kotlin.Boolean
import kotlin.IllegalStateException
import kotlin.String

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
class DependencyResolver(versionManager: VersionManager) {
    private val versionManager: VersionManager
    private var dependenciesGraph // the value is 'pluginId'
            : DirectedGraph<String>? = null
    private var dependentsGraph // the value is 'pluginId'
            : DirectedGraph<String>? = null
    private var resolved = false

    init {
        this.versionManager = versionManager
    }

    fun resolve(plugins: List<PluginDescriptor>): Result {
        // create graphs
        dependenciesGraph = DirectedGraph()
        dependentsGraph = DirectedGraph()

        // populate graphs
        val pluginByIds: MutableMap<String, PluginDescriptor> = HashMap<String, PluginDescriptor>()
        for (plugin: PluginDescriptor in plugins) {
            addPlugin(plugin)
            pluginByIds[plugin.pluginId] = plugin
        }
        Log.d { "Graph: $dependenciesGraph" }

        // get a sorted list of dependencies
        val sortedPlugins: List<String> = dependenciesGraph!!.reverseTopologicalSort()
        Log.d { "Plugins order: $sortedPlugins" }

        // create the result object
        val result = Result(sortedPlugins)
        resolved = true
        for (pluginId: String? in sortedPlugins) {
            if (!pluginByIds.containsKey(pluginId)) {
                pluginId?.let { result.addNotFoundDependency(it) }
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
                    result.addWrongDependencyVersion(
                        WrongDependencyVersion(
                            pluginId,
                            dependentId,
                            existingVersion,
                            requiredVersion
                        )
                    )
                }
            }
        }
        return result
    }

    /**
     * Retrieves the plugins ids that the given holder id directly depends on.
     *
     * @param pluginId the unique holder identifier, specified in its metadata
     * @return an immutable list of dependencies (new list for each call)
     */
    fun getDependencies(pluginId: String): List<String> {
        checkResolved()
        return ArrayList<String>(dependenciesGraph!!.getNeighbors(pluginId))
    }

    /**
     * Retrieves the plugins ids that the given content is a direct dependency of.
     *
     * @param pluginId the unique holder identifier, specified in its metadata
     * @return an immutable list of dependents (new list for each call)
     */
    fun getDependents(pluginId: String): MutableList<String> {
        checkResolved()
        return ArrayList(dependentsGraph!!.getNeighbors(pluginId))
    }

    /**
     * Check if an existing version of dependency is compatible with the required version (from holder descriptor).
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
                // Don't register optional plugins in the dependency graph to avoid automatic disabling of the holder,
                // if an optional dependency is missing.
                if (!dependency.isOptional) {
                    edgeAdded = true
                    dependenciesGraph?.addEdge(pluginId, dependency.isOptional.toString())
                    dependency.pluginId?.let { dependentsGraph!!.addEdge(it, pluginId) }
                }
            }

            // Register the holder without dependencies, if all of its dependencies are optional.
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

    private fun getDependencyVersionSupport(dependent: PluginDescriptor?, dependencyId: String): String {
        val dependencies: List<PluginDependency>? = dependent!!.dependencies
        for (dependency: PluginDependency in dependencies!!) {
            if (dependencyId == dependency.pluginId) {
                return dependency.pluginVersionSupport
            }
        }
        throw IllegalStateException(
            "Cannot find a dependency with id '" + dependencyId +
                    "' for holder '" + dependent.pluginId + "'"
        )
    }

    class Result internal constructor(val sortedPlugins: List<String>) {
        private var cyclicDependency = false
        private val notFoundDependencies // value is "pluginId"
                : MutableList<String>


        private val wrongVersionDependencies: MutableList<WrongDependencyVersion>

        init {
            notFoundDependencies = ArrayList()
            wrongVersionDependencies = ArrayList()
        }

        /**
         * Returns true is a cyclic dependency was detected.
         */
        fun hasCyclicDependency(): Boolean {
            return cyclicDependency
        }

        /**
         * Returns a list with dependencies required that were not found.
         */
        fun getNotFoundDependencies(): List<String> {
            return notFoundDependencies
        }

        /**
         * Returns a list with dependencies with wrong version.
         */
        fun getWrongVersionDependencies(): List<WrongDependencyVersion> {
            return wrongVersionDependencies
        }

        fun addNotFoundDependency(pluginId: String) {
            notFoundDependencies.add(pluginId)
        }

        fun addWrongDependencyVersion(wrongDependencyVersion: WrongDependencyVersion) {
            wrongVersionDependencies.add(wrongDependencyVersion)
        }
    }

    class WrongDependencyVersion internal constructor(
// value is "pluginId"
        val dependencyId: String, // value is "pluginId"
        var dependentId: String, existingVersion: String, requiredVersion: String,
    ) {
        val existingVersion: String
        val requiredVersion: String

        init {
            this.existingVersion = existingVersion
            this.requiredVersion = requiredVersion
        }

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
    class CyclicDependencyException() : PluginRuntimeException("Cyclic dependencies")

    /**
     * Indicates that the dependencies required were not found.
     */
    class DependenciesNotFoundException(dependencies: List<String>) :
        PluginRuntimeException("Dependencies '{}' not found", dependencies) {
        val dependencies: List<String>

        init {
            this.dependencies = dependencies
        }
    }

    /**
     * Indicates that some dependencies have wrong version.
     */
    class DependenciesWrongVersionException(dependencies: List<WrongDependencyVersion>) :
        PluginRuntimeException("Dependencies '{}' have wrong version", dependencies) {
        val dependencies: List<WrongDependencyVersion>

        init {
            this.dependencies = dependencies
        }
    }

}