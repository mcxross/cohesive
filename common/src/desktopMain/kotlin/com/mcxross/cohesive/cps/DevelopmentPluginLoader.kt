package com.mcxross.cohesive.cps

/**
 * Load all information needed by a corePlugin from [DevelopmentPluginClasspath].
 */
class DevelopmentPluginLoader(pluginManager: PluginManager) :
  BasePluginLoader(pluginManager, DevelopmentPluginClasspath())
