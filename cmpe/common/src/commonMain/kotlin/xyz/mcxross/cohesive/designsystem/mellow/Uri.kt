package xyz.mcxross.cohesive.designsystem.mellow

import io.ktor.utils.io.core.*

interface Uri {
  val path: String
  val mimeType: String?
}

expect class UriConverter() {
  fun toInput(uri: Uri): Input
  suspend fun name(uri: Uri): String
}
