package com.mcxross.cohesive.cps

import com.mcxross.cohesive.common.frontend.utils.exists
import com.mcxross.cohesive.common.utils.Log
import okio.Path
import java.io.IOException

/**
 * The default implementation for [PluginStatusProvider].
 * The enabled plugins are read from `enabled.txt` file and
 * the disabled plugins are read from `disabled.txt` file.
 */
class DefaultPluginStatusProvider(private val pluginsRoot: Path) : PluginStatusProvider {

  private val plugins = object {
    val enabled: MutableList<String> = mutableListOf()
    val disabled: MutableList<String> = mutableListOf()
    fun role(pluginKindStatus: PluginKindStatus) {
      enabled.addAll(pluginKindStatus.enabled.toMutableList())
      disabled.addAll(pluginKindStatus.disabled.toMutableList())
    }
  }
  val enabledFilePath: Path
    get() = getEnabledFilePath(pluginsRoot)
  val disabledFilePath: Path
    get() = getDisabledFilePath(pluginsRoot)

  init {
    try {
      val config = PluginStatus.holder()

      plugins.role(config.primaryKindStatus)
      plugins.role(config.secondaryKindStatus)
      plugins.role(config.tertiaryKindStatus)

      // create a list with plugin identifiers that should be only accepted by this manager (whitelist from plugins/enabled.txt file)
      //enabledPlugins = FileUtils.readLines(enabledFilePath, true).toMutableList()
      Log.i { "Enabled plugins: ${plugins.enabled}" }

      // create a list with plugin identifiers that should not be accepted by this manager (blacklist from plugins/disabled.txt file)
      //disabledPlugins = FileUtils.readLines(disabledFilePath, true).toMutableList()
      Log.i { "Disabled plugins: ${plugins.disabled}" }
    } catch (e: IOException) {
      Log.e { e.message.toString() }
    }
  }

  override fun isPluginDisabled(pluginId: String): Boolean {
    return if (plugins.disabled.contains(pluginId)) {
      true
    } else plugins.enabled.isNotEmpty() && !plugins.enabled.contains(pluginId)
  }

  override fun disablePlugin(pluginId: String) {
    if (isPluginDisabled(pluginId)) {
      // do nothing
      return
    }
    if (exists(enabledFilePath)) {
      plugins.enabled.remove(pluginId)
      try {
        //TODO: disable plugin in config
        //FileUtils.writeLines(plugins.enabled, enabledFilePath)
      } catch (e: IOException) {
        throw PluginRuntimeException(e)
      }
    } else {
      plugins.disabled.add(pluginId)
      try {
        //FileUtils.writeLines(plugins.disabled, disabledFilePath)
        //TODO: disable plugin in config
      } catch (e: IOException) {
        throw PluginRuntimeException(e)
      }
    }
  }

  override fun enablePlugin(pluginId: String) {
    if (!isPluginDisabled(pluginId)) {
      // do nothing
      return
    }
    if (exists(enabledFilePath)) {
      plugins.enabled.add(pluginId)
      try {
        //FileUtils.writeLines(plugins.enabled, enabledFilePath)
        //TODO: enable plugin in config
      } catch (e: IOException) {
        throw PluginRuntimeException(e)
      }
    } else {
      plugins.disabled.remove(pluginId)
      try {
        //FileUtils.writeLines(plugins.disabled, disabledFilePath)
        // TODO: enable plugin in config
      } catch (e: IOException) {
        throw PluginRuntimeException(e)
      }
    }
  }

  companion object {
    fun getEnabledFilePath(pluginsRoot: Path): Path {
      return pluginsRoot.resolve("enabled.txt")
    }

    fun getDisabledFilePath(pluginsRoot: Path): Path {
      return pluginsRoot.resolve("disabled.txt")
    }
  }
}
