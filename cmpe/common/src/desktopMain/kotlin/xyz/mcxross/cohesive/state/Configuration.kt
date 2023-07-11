package xyz.mcxross.cohesive.state

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object Configuration {
  var asSetFor: ConfigurationContainer? = null
}

@Serializable
data class ConfigurationContainer(val cohesive: ConfigCohesive, val platform: ConfigPlatform)

@Serializable
data class ConfigCohesive(
  val repo: String = "https://raw.githubusercontent.com/mcxross/cohesives/main/src/res/",
  @SerialName("primary") val primaryPluginContainer: PluginContainer,
  @SerialName("secondary") val secondaryPluginContainer: PluginContainer
)

@Serializable
data class ConfigPlatform(
  val k: List<String> = emptyList(),
  val v: List<String> = emptyList(),
  @SerialName("tertiary") val tertiaryPluginContainer: PluginContainer
)

@Serializable data class PluginContainer(val enabled: List<String>, val disabled: List<String>)
