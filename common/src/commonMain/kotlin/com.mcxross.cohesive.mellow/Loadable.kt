package com.mcxross.cohesive.mellow

import androidx.compose.runtime.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay

@Composable
fun <T : Any> loadable(
    load: () -> T,
): MutableState<T?> {
    return loadableScoped { load() }
}

private val loadingKey = Any()

@Composable
fun <T : Any> loadableScoped(
    load: CoroutineScope.() -> T,
): MutableState<T?> {
    val state: MutableState<T?> = remember { mutableStateOf(null) }
    LaunchedEffect(loadingKey) {
        delay(300)
        try {
            state.value = load()
        } catch (e: CancellationException) {
            // ignore
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    return state
}