package com.mcxross.cohesive.cps

import java.nio.file.Path


class PluginAlreadyLoadedException(val pluginId: String, val pluginPath: Path) :
    PluginRuntimeException("Plugin '{}' already loaded with id '{}'", pluginPath, pluginId)