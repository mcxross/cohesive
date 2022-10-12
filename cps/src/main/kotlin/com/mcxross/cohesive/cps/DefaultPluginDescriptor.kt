package com.mcxross.cohesive.cps

import java.util.*

open class DefaultPluginDescriptor() : PluginDescriptor {
    override var pluginId: String = ""
    override var pluginDescription: String? = null

    /**
     * Returns the name of the class that implements Plugin interface.
     */
    override var pluginClass: String = Plugin::class.java.name
    override var version: String = ""

    /**
     * Returns string version of requires
     *
     * @return String with requires expression on SemVer format
     */
    override var requires = "*" // SemVer format
    override var provider: String? = null
    override var dependencies: MutableList<PluginDependency> = mutableListOf()
    override var license: String = ""

    init {
        dependencies = ArrayList<PluginDependency>()
    }

    constructor(
        pluginId: String,
        pluginDescription: String,
        pluginClass: String,
        version: String,
        requires: String,
        provider: String,
        license: String,
    ) : this() {
        this.pluginId = pluginId
        this.pluginDescription = pluginDescription
        this.pluginClass = pluginClass
        this.version = version
        this.requires = requires
        this.provider = provider
        this.license = license
    }

    fun addDependency(dependency: PluginDependency) {
        dependencies.add(dependency)
    }


    override fun toString(): String {
        return ("PluginDescriptor [pluginId=" + pluginId + ", pluginClass="
                + pluginClass + ", version=" + version + ", provider="
                + provider + ", dependencies=" + dependencies + ", description="
                + pluginDescription + ", requires=" + requires + ", license="
                + license + "]")
    }

    protected fun setPluginId(pluginId: String): DefaultPluginDescriptor {
        this.pluginId = pluginId
        return this
    }

    protected fun setPluginDescription(pluginDescription: String): PluginDescriptor {
        this.pluginDescription = pluginDescription
        return this
    }

    protected fun setPluginClass(pluginClassName: String): PluginDescriptor {
        pluginClass = pluginClassName
        return this
    }

    protected fun setProvider(provider: String?): PluginDescriptor {
        this.provider = provider
        return this
    }

    protected fun setRequires(requires: String): PluginDescriptor {
        this.requires = requires
        return this
    }

    fun setDependencies(dependencies: String): PluginDescriptor {
        var dependenciesStr = dependencies
        this.dependencies = ArrayList<PluginDependency>()
        dependenciesStr = dependencies.trim { it <= ' ' }
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
        return this
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DefaultPluginDescriptor) return false
        return (pluginId == other.pluginId) && pluginDescription == other.pluginDescription && pluginClass == other.pluginClass && version == other.version && requires == other.requires && provider == other.provider && dependencies == other.dependencies && license == other.license
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
            license
        )
    }
}
