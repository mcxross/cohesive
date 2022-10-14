package com.mcxross.cohesive.cps

import java.nio.file.Path
import java.util.function.BooleanSupplier

fun compositePluginRepository(
    repo: CompositePluginRepository.() -> Unit,
): PluginRepository {
    val repository = CompositePluginRepository()
    repo(repository)
    return repository
}

/**
 * A [PluginRepository] that delegates to a list of other [PluginRepository]s.
 * A plugin repository that can be used to combine multiple repositories into one.
 * Think of it as a container for other repositories.
 * The [PluginRepository]s are cached in a [MutableList]. Prefer using the DSL to add repositories.
 */
class CompositePluginRepository : PluginRepository {

    private val repositories: MutableList<PluginRepository> = ArrayList()
    override val pluginPaths: List<Path>
        get() {
            val paths: MutableSet<Path> = LinkedHashSet()
            for (repository in repositories) {
                paths.addAll(repository.pluginPaths)
            }
            return ArrayList(paths)
        }

    /**
     * Adds a [PluginRepository] to this [CompositePluginRepository] repository.
     * @param repository The [PluginRepository] to add.
     * @return [Unit]
     */
    fun plus(repository: PluginRepository) = repositories.add(repository)

    /**
     * Add a [PluginRepository] to this [CompositePluginRepository] only if `condition` is true.
     * @param repository The [PluginRepository] to add.
     * @param condition The condition to check.
     * @return [Unit]
     */
    fun plus(repository: PluginRepository, condition: BooleanSupplier) {
        if (condition.asBoolean)
            plus(repository)

    }


    /**
     * Clear given pluginPath from repository.
     * @param pluginPath The path to clear.
     * @return [Boolean] true if the path was cleared, false otherwise.
     */
    override fun deletePluginPath(pluginPath: Path): Boolean {
        for (repository in repositories) {
            if (repository.deletePluginPath(pluginPath)) {
                return true
            }
        }
        return false
    }
}