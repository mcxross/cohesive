package com.mcxross.cohesive.cps

enum class PluginState(private val status: String) {
  /**
   * The runtime knows the Plugin is there. It knows about the Plugin path, the Plugin
   * descriptor.
   */
  CREATED("CREATED"),

  /** The Plugin cannot be used. */
  DISABLED("DISABLED"),

  /**
   * The Plugin is created. All the dependencies are created and resolved. The Plugin is
   * ready to be started.
   */
  RESOLVED("RESOLVED"),

  /** The [Plugin.start] has executed. A started Plugin may contribute extensions. */
  STARTED("STARTED"),

  /** The [Plugin.stop] has executed. */
  STOPPED("STOPPED"),

  /** Plugin failed to start. */
  FAILED("FAILED");

  fun equals(status: String?): Boolean {
    return if (status == null) false else this.status.equals(status, ignoreCase = true)
  }

  override fun toString(): String {
    return status
  }

  companion object {
    fun parse(string: String?): PluginState? {
      for (status in PluginState.values()) {
        if (status.toString() == string) {
          return status
        }
      }
      return null
    }
  }
}
