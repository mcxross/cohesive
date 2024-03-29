package xyz.mcxross.cohesive.cps

import com.akuleshov7.ktoml.Toml
import com.akuleshov7.ktoml.TomlConfig
import xyz.mcxross.cohesive.utils.readFileToStr
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer

interface PluginKindStatus {
  val enabled: List<String>
  val disabled: List<String>
}

@Serializable
data class PluginKindsStatusHolder(
  @SerialName("primary") val kindStatus: KindStatus,
  @SerialName("secondary") val secondaryKindStatus: KindStatus,
)

@Serializable
data class KindStatus(
  @SerialName("enabled") override val enabled: List<String>,
  @SerialName("disabled") override val disabled: List<String>,
) : PluginKindStatus

object PluginStatus {
  fun holder(): PluginKindsStatusHolder {

    return Toml(
        config =
          TomlConfig(
            // allow/prohibit unknown names during the deserialization, default false
            ignoreUnknownNames = true,
            // allow/prohibit empty values like "a = # comment", default true
            allowEmptyValues = true,
            // allow/prohibit escaping of single quotes in literal strings, default true
            allowEscapedQuotesInLiteralStrings = true,
            // allow/prohibit processing of empty toml, if false - throws an
            // InternalDecodingException exception, default is true
            allowEmptyToml = true,
            indentation = TomlConfig.Indentation.FOUR_SPACES,
          ),
      )
      .partiallyDecodeFromString(
        deserializer = serializer(),
        toml = readFileToStr("config.toml").trimMargin(),
        tomlTableName = "cohesive",
      )
  }
}
