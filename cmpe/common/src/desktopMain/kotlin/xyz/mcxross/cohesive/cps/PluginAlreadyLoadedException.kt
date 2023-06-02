package xyz.mcxross.cohesive.cps

import okio.Path

class PluginAlreadyLoadedException(val pluginId: String, val pluginPath: Path) :
  PluginRuntimeException("Plugin '{}' already loaded with id '{}'", pluginPath, pluginId)
