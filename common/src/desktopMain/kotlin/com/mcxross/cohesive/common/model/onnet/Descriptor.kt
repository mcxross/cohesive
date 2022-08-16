package com.mcxross.cohesive.common.model.onnet

import androidx.compose.runtime.mutableStateOf
import com.mcxross.cohesive.common.model.Chain
import com.mcxross.cohesive.common.utils.runBlocking
import io.ktor.client.*
import io.ktor.client.engine.cio.*
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
    private var chains: ArrayList<Chain> = arrayListOf()
    fun run(): Descriptor {
        scope.launch(Dispatchers.IO) {
            chainFlow().toList(chains)
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

    private suspend fun chainFlow(): Flow<Chain> = flow {
        var jsonResp = ""
        val desCo = scope.launch(Dispatchers.IO) {
            jsonResp = getDescriptor()
        }
        desCo.join()

        parse(jsonResp).jsonObject["chain"]?.jsonArray?.forEach {
            emit(
                Chain(
                    name = parse(it.toString()).jsonObject["name"].toString(),
                    category = parse(it.toString()).jsonObject["category"].toString(),
                    icon = parse(it.toString()).jsonObject["icon"].toString()
                )
            )
        }

    }

    fun getChains(): List<Chain> {
        return chains
    }

}

actual fun getDescriptor(): String {

    return runBlocking {
        HttpClient(CIO).use { client ->
            client.get("https://raw.githubusercontent.com/mcxross/cohesives/main/src/descriptor.json").bodyAsText()
        }
    }
}


