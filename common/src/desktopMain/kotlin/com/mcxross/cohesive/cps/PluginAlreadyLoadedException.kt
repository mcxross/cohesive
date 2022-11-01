package com.mcxross.cohesive.cps

import okio.Path

class PluginAlreadyLoadedException(val pluginId: String, val pluginPath: Path) :
  PluginRuntimeException("CorePlugin '{}' already loaded with id '{}'", pluginPath, pluginId)
