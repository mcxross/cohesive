package com.mcxross.cohesive.common.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import io.ktor.utils.io.errors.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext


@Composable
fun <T> CImage(
    load: suspend () -> T,
    painterFor: @Composable (T) -> Painter,
    contentDescription: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    coroutineDispatcher: CoroutineDispatcher,
) {
    var loading: Boolean by remember { mutableStateOf(true) }
    if (loading) {
        Box {
            CProgress(modifier = Modifier.align(Alignment.Center))
        }
    }

    val image: T? by produceState<T?>(null) {
        value = withContext(coroutineDispatcher) {
            try {
                load()
            } catch (e: IOException) {
                loading = false
                e.printStackTrace()
                null
            }
        }
    }

    if (image != null) {
        loading = false
        Image(
            painter = painterFor(image!!),
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier
        )
    }
}



