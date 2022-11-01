package com.mcxross.cohesive.cps

enum class PluginState(private val status: String) {
  /**
   * The runtime knows the corePlugin is there. It knows about the corePlugin path, the corePlugin descriptor.
   */
  CREATED("CREATED"),

  /**
   * The corePlugin cannot be used.
   */
  DISABLED("DISABLED"),

  /**
   * The corePlugin is created. All the dependencies are created and resolved.
   * The corePlugin is ready to be started.
   */
  RESOLVED("RESOLVED"),

  /**
   * The [CorePlugin.start] has executed. A started corePlugin may contribute extensions.
   */
  STARTED("STARTED"),

  /**
   * The [CorePlugin.stop] has executed.
   */
  STOPPED("STOPPED"),

  /**
   * CorePlugin failed to start.
   */
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
