package com.mcxross.cohesive.cps

import java.util.*

/**
 * The default implementation of the [PluginDescriptor] interface
 * @param pluginId the corePlugin id. Defaults to an empty String.
 * @param pluginDescription the corePlugin description. Defaults to an empty String.
 * @param corePluginClass the corePlugin class. Defaults to [CorePlugin::class.java.name].
 * @param version String version of requires. String with requires expression on SemVer format
 * @param requires the corePlugin dependencies. Defaults to a '*'.
 * @param provider the corePlugin provider. Defaults to an empty String.
 * @param license the corePlugin license. Defaults to an empty String.
 * */
open class DefaultPluginDescriptor(
  override var pluginId: String = "",
  override var pluginDescription: String = "",
  override var pluginClass: String = CorePlugin::class.java.name,
  override var version: String = "",
  override var requires: String = "*",
  override var provider: String = "",
  override var license: String = "",
) : PluginDescriptor {

  override var dependencies: MutableList<PluginDependency> = mutableListOf()

  override fun toString(): String {
    return ("PluginDescriptor [pluginId=" + pluginId + ", corePluginClass="
      + pluginClass + ", version=" + version + ", provider="
      + provider + ", dependencies=" + dependencies + ", description="
      + pluginDescription + ", requires=" + requires + ", license="
      + license + "]")
  }

  fun setDependencies(dependencies: String) {
    dependencies.trim { it <= ' ' }
    if (dependencies.isNotEmpty()) {
      val tokens = dependencies.split(",".toRegex()).dropLastWhile { it.isEmpty() }
        .toTypedArray()
      for (dependency in tokens) {
        dependency.trim { it <= ' ' }
        if (dependency.isNotEmpty()) {
          this.dependencies.add(PluginDependency(dependency))
        }
      }
    }
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is DefaultPluginDescriptor) return false
    return (pluginId == other.pluginId) && (pluginDescription == other.pluginDescription) && (pluginClass == other.pluginClass) && (version == other.version) && (requires == other.requires) && (provider == other.provider) && (dependencies == other.dependencies) && (license == other.license)
  }

  override fun hashCode(): Int {
    return Objects.hash(
      pluginId,
      pluginDescription,
      pluginClass,
      version,
      requires,
      provider,
      dependencies,
      license,
    )
  }
}
