package com.mcxross.cohesive.common.frontend.utils

import com.akuleshov7.ktoml.Toml
import com.akuleshov7.ktoml.TomlConfig
import kotlinx.serialization.decodeFromString

inline fun <reified T> load(
  tomlString: String,
): T {
  return Toml(
    config = TomlConfig(
      ignoreUnknownNames = true,
      allowEmptyValues = true,
      allowEscapedQuotesInLiteralStrings = true,
      allowEmptyToml = true,
    ),
  ).decodeFromString(
    tomlString,
  )
}
