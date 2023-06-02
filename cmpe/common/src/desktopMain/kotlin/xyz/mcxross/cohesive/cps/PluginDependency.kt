package xyz.mcxross.cohesive.cps

import java.util.*


class PluginDependency(dependency: String) {
  var pluginId: String? = null
  var pluginVersionSupport = "*"
  val isOptional: Boolean

  init {
    val index = dependency.indexOf('@')
    if (index == -1) {
      pluginId = dependency
    } else {
      pluginId = dependency.substring(0, index)
      if (dependency.length > index + 1) {
        pluginVersionSupport = dependency.substring(index + 1)
      }
    }

    // A dependency is considered as optional, if the Plugin id ends with a question mark.
    isOptional = pluginId!!.endsWith("?")
    if (isOptional) {
      pluginId = pluginId!!.substring(0, pluginId!!.length - 1)
    }
  }

  override fun toString(): String {
    return ("PluginDependency [pluginId=" + pluginId + ", pluginVersionSupport="
      + pluginVersionSupport + ", optional="
      + isOptional + "]")
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is PluginDependency) return false
    return (isOptional == other.isOptional) && pluginId == other.pluginId && pluginVersionSupport == other.pluginVersionSupport
  }

  override fun hashCode(): Int {
    return Objects.hash(pluginId, pluginVersionSupport, isOptional)
  }
}
