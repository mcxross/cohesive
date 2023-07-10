package xyz.mcxross.cohesive.designsystem.mellow

import io.ktor.utils.io.core.*
import io.ktor.utils.io.streams.*
import java.io.File
import java.nio.file.Files

actual class UriConverter {
  actual fun toInput(uri: Uri): Input = when {
    uri.path.startsWith("http") -> TODO("unimplemented")
    else -> File(uri.path).inputStream().asInput()
  }

  actual suspend fun name(uri: Uri): String = when {
    uri.path.startsWith("http") -> TODO("unimplemented")
    else -> File(uri.path).name
  }

}

data class FileUri(
  val file: File
) : Uri {
  override val path: String
    get() = file.path
  override val mimeType: String?
    get() = Files.probeContentType(file.toPath())
}
