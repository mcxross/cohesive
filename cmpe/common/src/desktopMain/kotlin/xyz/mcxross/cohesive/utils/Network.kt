package xyz.mcxross.cohesive.utils

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.loadSvgPainter
import androidx.compose.ui.res.loadXmlImageVector
import androidx.compose.ui.unit.Density
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.network.sockets.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.xml.sax.InputSource
import java.io.ByteArrayInputStream

val ktorHttpClient = HttpClient {}

actual fun isInternetAvailable(): Boolean {
  return runBlocking {
    try {
      ktorHttpClient.head("http://google.com")
      true
    } catch (e: Exception) {
      println(e.message)
      false
    }
  }
}

actual fun getHTTPClient(): HttpClient = HttpClient(CIO) {
  install(UserAgent) {
    agent = "Cohesive"
  }
  install(HttpRequestRetry) {
    maxRetries = 5
    retryIf { _, response ->
      !response.status.isSuccess()
    }
    retryOnExceptionIf { _, cause ->
      cause is ConnectTimeoutException
    }
    exponentialDelay()
  }
}


suspend fun loadImageBitmap(
  url: String,
): ImageBitmap =
  urlStream(url).use(::loadImageBitmap)

suspend fun loadSvgPainter(
  url: String,
  density: Density,
): Painter =
  urlStream(url).use { loadSvgPainter(it, density) }

suspend fun loadXmlImageVector(
  url: String,
  density: Density,
): ImageVector =
  urlStream(url).use { loadXmlImageVector(InputSource(it), density) }

private suspend fun urlStream(
  url: String,
) = getHTTPClient().use {
  ByteArrayInputStream(it.get(url).body())
}
