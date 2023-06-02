package xyz.mcxross.cohesive.cps

import xyz.mcxross.cohesive.common.utils.Log
import okio.Path

inline fun compositePluginRepository(
  repo: CompositePluginRepository.() -> Unit,
): PluginRepository {
  val compositePluginRepository = CompositePluginRepository()
  compositePluginRepository.repo()
  return compositePluginRepository
}

/**
 * A [PluginRepository] that delegates to a list of other [PluginRepository]s. A Plugin
 * repository that can be used to combine multiple repositories into one. Think of it as a container
 * for other repositories. The [PluginRepository]s are cached in a [MutableList]. Prefer using the
 * DSL to add repositories.
 */
class CompositePluginRepository : PluginRepository {

  private val repositories: MutableList<PluginRepository> = ArrayList()
  override val pluginPaths: List<Path>
    get() {
      val paths: MutableSet<Path> = LinkedHashSet()
      repositories.forEach {
        Log.d { "CompositePluginRepository: pluginPaths: ${it.pluginPaths}" }
        paths.addAll(it.pluginPaths)
      }
      return ArrayList(paths)
    }

  /**
   * Add a [PluginRepository] to this [CompositePluginRepository] only if `condition` in container
   * is true.
   * @param container The [PluginRepositoryContainer] to add.
   */
  operator fun plus(container: PluginRepositoryContainer) {
    if (container.condition.asBoolean) repositories.add(container.repo)
  }

  /**
   * Clear given pluginPath from repository.
   * @param pluginPath The path to clear.
   * @return [Boolean] true if the path was cleared, false otherwise.
   */
  override fun deletePluginPath(pluginPath: Path): Boolean {
    repositories.forEach {
      if (it.deletePluginPath(pluginPath)) {
        return true
      }
    }
    return false
  }
}
