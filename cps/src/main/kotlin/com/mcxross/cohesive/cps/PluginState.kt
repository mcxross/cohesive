package com.mcxross.cohesive.cps

enum class PluginState(private val status: String) {
    /**
     * The runtime knows the holder is there. It knows about the holder path, the holder descriptor.
     */
    CREATED("CREATED"),

    /**
     * The holder cannot be used.
     */
    DISABLED("DISABLED"),

    /**
     * The holder is created. All the dependencies are created and resolved.
     * The holder is ready to be started.
     */
    RESOLVED("RESOLVED"),

    /**
     * The [Plugin.start] has executed. A started holder may contribute extensions.
     */
    STARTED("STARTED"),

    /**
     * The [Plugin.stop] has executed.
     */
    STOPPED("STOPPED"),

    /**
     * Plugin failed to start.
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