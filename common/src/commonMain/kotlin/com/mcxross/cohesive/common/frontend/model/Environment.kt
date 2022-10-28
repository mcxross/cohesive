package com.mcxross.cohesive.common.frontend.model


/**
 * A Single Source of Truth for the execution environment i.e.
 * the platform the app is running on, the Primary plugin,
 * and the configurations from the config file, config.toml, etc.
 * Reads via Context should be preferred over direct access.
 * @property chains A list of Chain flavor.
 * @property chainPaths A list of Chain flavor URL paths.
 * @property plugins A list of Primary Plugins available(installed and non-installed).
 */
object Environment {
  var chains: List<String> = emptyList()
  var chainPaths: List<String> = emptyList()
  var plugins: List<Plugin> = emptyList()

  /**
   * A builder method for the Environment object.
   * This return an Environment object at a go with the specified param(s)
   * @param [chains] a list of Chain flavors. Defaults to emptyList
   * @param [chainPaths] a list of chain flavor URL paths. Defaults to emptyList*/
  fun create(
    chains: List<String> = emptyList(),
    chainPaths: List<String> = emptyList(),
  ): Environment {
    val environment = Environment
    Environment.chains = chains
    Environment.chainPaths = chainPaths
    return environment
  }
}
