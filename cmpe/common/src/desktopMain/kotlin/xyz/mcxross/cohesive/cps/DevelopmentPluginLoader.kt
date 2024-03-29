package xyz.mcxross.cohesive.cps

/** Load all information needed by a Plugin from [DevelopmentPluginClasspath]. */
class DevelopmentPluginLoader(pluginManager: PluginManager) :
  BasePluginLoader(pluginManager, DevelopmentPluginClasspath())
