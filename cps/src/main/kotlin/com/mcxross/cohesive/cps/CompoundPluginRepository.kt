package com.mcxross.cohesive.cps

import java.nio.file.Path
import java.util.function.BooleanSupplier


class CompoundPluginRepository : PluginRepository {
    private val repositories: MutableList<PluginRepository> = ArrayList()
    fun add(repository: PluginRepository?): CompoundPluginRepository {
        requireNotNull(repository) { "null not allowed" }
        repositories.add(repository)
        return this
    }

    /**
     * Add a [PluginRepository] only if the `condition` is satisfied.
     *
     * @param repository
     * @param condition
     * @return
     */
    fun add(repository: PluginRepository, condition: BooleanSupplier): CompoundPluginRepository {
        return if (condition.asBoolean) {
            add(repository)
        } else this
    }

    override val pluginPaths: List<Path>
        get() {
            val paths: MutableSet<Path> = LinkedHashSet()
            for (repository in repositories) {
                paths.addAll(repository.pluginPaths)
            }
            return ArrayList(paths)
        }

    override fun deletePluginPath(pluginPath: Path): Boolean {
        for (repository in repositories) {
            if (repository.deletePluginPath(pluginPath)) {
                return true
            }
        }
        return false
    }
}