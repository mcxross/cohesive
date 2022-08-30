package com.mcxross.cohesive.common.utils

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import com.akuleshov7.ktoml.Toml
import com.akuleshov7.ktoml.TomlConfig

fun load(tomlString: String): Configuration {
    return Toml(
        config = TomlConfig(
            ignoreUnknownNames = true,
            allowEmptyValues = true,
            allowEscapedQuotesInLiteralStrings = true,
            allowEmptyToml = true,
        ),
    ).decodeFromString(
        tomlString
    )
}

@Serializable
data class Configuration(val chain: Chain)

@Serializable
data class Chain(
    @SerialName("clusterKey") val clusterKey: List<String?>,
    @SerialName("clusterValue") val clusterValue: List<String>
)
