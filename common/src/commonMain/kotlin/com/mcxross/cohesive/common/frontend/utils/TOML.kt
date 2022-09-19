package com.mcxross.cohesive.common.frontend.utils

import com.akuleshov7.ktoml.Toml
import com.akuleshov7.ktoml.TomlConfig
import com.mcxross.cohesive.common.frontend.model.Configuration
import kotlinx.serialization.decodeFromString

fun load(
    tomlString: String,
): Configuration {
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