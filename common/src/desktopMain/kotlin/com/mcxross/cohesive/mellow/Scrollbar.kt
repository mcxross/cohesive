package com.mcxross.cohesive.mellow

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.ScrollbarAdapter
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun VerticalScrollbar(
    modifier: Modifier,
    scrollState: ScrollState,
) = androidx.compose.foundation.VerticalScrollbar(
    adapter = rememberScrollbarAdapter(scrollState),
    modifier = modifier
)

@Composable
actual fun VerticalScrollbar(
    modifier: Modifier,
    scrollState: LazyListState,
) = androidx.compose.foundation.VerticalScrollbar(
    adapter = rememberScrollbarAdapter(scrollState),
    modifier = modifier,
)

@Composable
fun VerticalScrollbar(
    scrollbarAdapter: ScrollbarAdapter,
    modifier: Modifier,
) = androidx.compose.foundation.VerticalScrollbar(
    scrollbarAdapter,
    modifier,
)

@Composable
actual fun HorizontalScrollbar(
    modifier: Modifier,
    scrollState: ScrollState,
) = androidx.compose.foundation.HorizontalScrollbar(
    adapter = rememberScrollbarAdapter(scrollState),
    modifier = modifier,
)

@Composable
actual fun HorizontalScrollbar(
    modifier: Modifier,
    scrollState: LazyListState,
) = androidx.compose.foundation.HorizontalScrollbar(
    adapter = rememberScrollbarAdapter(scrollState),
    modifier = modifier,
)