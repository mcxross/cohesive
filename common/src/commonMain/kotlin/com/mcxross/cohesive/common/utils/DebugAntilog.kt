package com.mcxross.cohesive.common.utils

import io.github.aakira.napier.Antilog
import java.util.logging.Handler

expect class DebugAntilog(
defaultTag: String = "Cohesive",
  handler: List<Handler> = listOf()
) : Antilog {
  constructor(defaultTag: String)
}

