package com.mcxross.cohesive.cps

import com.mcxross.cohesive.cps.utils.FileUtils
import com.mcxross.cohesive.cps.utils.Log
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

/**
 * The default implementation for [PluginStatusProvider].
 * The enabled plugins are read from `enabled.txt` file and
 * the disabled plugins are read from `disabled.txt` file.
 */
class DefaultPluginStatusProvider(private val pluginsRoot: Path) : PluginStatusProvider {
    private var enabledPlugins: MutableList<String>  = mutableListOf()
    private var disabledPlugins: MutableList<String> = mutableListOf()

    init {
        try {
            // create a list with plugin identifiers that should be only accepted by this manager (whitelist from plugins/enabled.txt file)
            enabledPlugins = FileUtils.readLines(enabledFilePath, true).toMutableList()
            Log.i { "Enabled plugins: $enabledPlugins" }

            // create a list with plugin identifiers that should not be accepted by this manager (blacklist from plugins/disabled.txt file)
            disabledPlugins = FileUtils.readLines(disabledFilePath, true).toMutableList()
            Log.i { "Disabled plugins: $disabledPlugins" }
        } catch (e: IOException) {
            Log.e { e.message.toString() }
        }
    }

    override fun isPluginDisabled(pluginId: String): Boolean {
        return if (disabledPlugins.contains(pluginId)) {
            true
        } else enabledPlugins.isNotEmpty() && !enabledPlugins.contains(pluginId)
    }

    override fun disablePlugin(pluginId: String) {
        if (isPluginDisabled(pluginId)) {
            // do nothing
            return
        }
        if (Files.exists(enabledFilePath)) {
            enabledPlugins.remove(pluginId)
            try {
                FileUtils.writeLines(enabledPlugins, enabledFilePath)
            } catch (e: IOException) {
                throw PluginRuntimeException(e)
            }
        } else {
            disabledPlugins.add(pluginId)
            try {
                FileUtils.writeLines(disabledPlugins, disabledFilePath)
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
        if (Files.exists(enabledFilePath)) {
            enabledPlugins.add(pluginId)
            try {
                FileUtils.writeLines(enabledPlugins, enabledFilePath)
            } catch (e: IOException) {
                throw PluginRuntimeException(e)
            }
        } else {
            disabledPlugins.remove(pluginId)
            try {
                FileUtils.writeLines(disabledPlugins, disabledFilePath)
            } catch (e: IOException) {
                throw PluginRuntimeException(e)
            }
        }
    }

    val enabledFilePath: Path
        get() = getEnabledFilePath(pluginsRoot)
    val disabledFilePath: Path
        get() = getDisabledFilePath(pluginsRoot)

    companion object {
        fun getEnabledFilePath(pluginsRoot: Path): Path {
            return pluginsRoot.resolve("enabled.txt")
        }

        fun getDisabledFilePath(pluginsRoot: Path): Path {
            return pluginsRoot.resolve("disabled.txt")
        }
    }
}