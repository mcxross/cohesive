package com.mcxross.cohesive.common.model.onnet

import androidx.compose.runtime.mutableStateOf
import com.mcxross.cohesive.common.model.Plugin
import com.mcxross.cohesive.common.utils.runBlocking
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.network.sockets.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
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
    private var plugins: ArrayList<Plugin> = arrayListOf()
    fun run(): Descriptor {
        scope.launch(Dispatchers.IO) {
            chainFlow().toList(plugins)
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
        return parse(jsonElement.toString()).jsonObject[key].toString()
    }


    private suspend fun chainFlow(): Flow<Plugin> = flow {
        var jsonResp = ""
        val desCo = scope.launch(Dispatchers.IO) {
            jsonResp = getDescriptor()
        }
        desCo.join()

        parse(jsonResp).jsonObject["chain"]?.jsonArray?.forEach {
            emit(
                Plugin(
                    id = getValue(jsonElement = it, key = "id"),
                    name = getValue(jsonElement = it, key = "name"),
                    icon = getValue(jsonElement = it, key = "icon"),
                    category = getValue(jsonElement = it, key = "category"),
                    description = getValue(jsonElement = it, key = "description"),
                    author = getValue(jsonElement = it, key = "author"),
                )
            )
        }

    }

    fun getPlugins(): List<Plugin> {
        return plugins
    }

}

actual fun getDescriptor(): String {

    return runBlocking {
        HttpClient(CIO) {
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
        }.use { client ->
            client.get("https://raw.githubusercontent.com/mcxross/cohesives/main/src/descriptor.json").bodyAsText()
        }
    }
}


