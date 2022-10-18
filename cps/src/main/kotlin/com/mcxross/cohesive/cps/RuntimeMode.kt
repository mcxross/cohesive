package com.mcxross.cohesive.cps


enum class RuntimeMode(
// deployment
    private val names: String, vararg val aliases: String,
) {
    DEVELOPMENT("development", "dev"),  // development
    DEPLOYMENT("deployment", "prod");

    override fun toString(): String {
        return name
    }

    companion object {
        private val map: MutableMap<String, RuntimeMode> = HashMap()

        init {
            RuntimeMode.values().forEach {
                map[it.name] = it
                for (alias: String in it.aliases) {
                    map[alias] = it
                }
            }
        }

        fun byName(name: String): RuntimeMode? {
            if (map.containsKey(name)) {
                return map[name]
            }
            throw NoSuchElementException(
                "Cannot found CPS runtime mode with name '" + name + "'." +
                        "Must be one value from '" + map.keys + "."
            )
        }
    }
}