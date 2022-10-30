package com.mcxross.cohesive.common.frontend.model.onnet

import androidx.compose.runtime.mutableStateOf
import com.mcxross.cohesive.common.frontend.utils.getHTTPClient
import com.mcxross.cohesive.common.frontend.utils.runBlocking
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

object Descriptor {
  private val isContentReady = mutableStateOf(false)
  private val scope = CoroutineScope(Dispatchers.IO)
  private var secondaryPlugins: ArrayList<com.mcxross.cohesive.common.frontend.model.SecondaryPlugin> = arrayListOf()
  fun run(): Descriptor {
    scope.launch(Dispatchers.IO) {
      chainFlow().toList(secondaryPlugins)
      onContentReady()
    }
    return this
  }

  private fun onContentReady() {
    isContentReady.value = true
  }

  fun isContentReady(): Boolean {
    return isContentReady.value
  }

  private fun parse(data: String): JsonElement {
    return Json.parseToJsonElement(data)
  }

  fun getValue(jsonElement: JsonElement, key: String): String {
    return parse(jsonElement.toString()).jsonObject[key].toString().replace("\"", "")
  }

  private suspend fun chainFlow(): Flow<com.mcxross.cohesive.common.frontend.model.SecondaryPlugin> = flow {
    var jsonResp = ""
    val desCo = scope.launch(Dispatchers.IO) { jsonResp = getDescriptor() }
    desCo.join()

    parse(jsonResp).jsonObject["chain"]?.jsonArray?.shuffled()?.forEach {
      emit(
        com.mcxross.cohesive.common.frontend.model.SecondaryPlugin(
          id = getValue(jsonElement = it, key = "id"),
          name = getValue(jsonElement = it, key = "name"),
          icon = getValue(jsonElement = it, key = "icon"),
          repo = getValue(jsonElement = it, key = "repo"),
          category = getValue(jsonElement = it, key = "category"),
          description = getValue(jsonElement = it, key = "description"),
          author = getValue(jsonElement = it, key = "author"),
        ),
      )
    }
  }

  fun getPlugins(): List<com.mcxross.cohesive.common.frontend.model.SecondaryPlugin> {
    return secondaryPlugins
  }
}

actual fun getDescriptor(): String {

  return runBlocking {
    getHTTPClient().use { client ->
      client
        .get("https://raw.githubusercontent.com/mcxross/cohesives/main/src/descriptor.json")
        .bodyAsText()
    }
  }
}
