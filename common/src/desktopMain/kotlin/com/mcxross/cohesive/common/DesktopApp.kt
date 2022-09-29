package com.mcxross.cohesive.common

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mcxross.cohesive.mellow.Button
import com.mcxross.cohesive.mellow.VerticalScrollbar
import kotlinx.coroutines.launch

@Preview
@Composable
fun AppPreview() {
    App()
}

@Preview
@Composable
fun Button() {
    Button({}, modifier = Modifier.height(35.dp), text = "Ok") {}
}


@OptIn(ExperimentalFoundationApi::class)

@Composable
fun Grid(
    fontSize: TextUnit = 12.sp,
) {
    Box {
        //Scroll states
        val lazyListScrollState = rememberLazyListState()
        val textFieldScrollState = rememberScrollState(0)
        //We need raw values to calculate the scroll position
        val scrollbarAdapter by remember { mutableStateOf(ScrollbarAdapter(lazyListScrollState)) }

        //We need to launch a coroutine to update the scroll position
        val coroutineScope = rememberCoroutineScope()

        Row {
            LazyColumn(
                state = lazyListScrollState
            ) {
                items(100) {
                    Text("Item $it")
                }
            }
            Column(
                modifier = Modifier
                    .background(Color.LightGray)
                    .verticalScroll(textFieldScrollState)
            ) {
                Text(
                    "import androidx.compose.desktop.ui.tooling.preview.Preview\n" +
                            "import androidx.compose.foundation.ExperimentalFoundationApi\n" +
                            "import androidx.compose.foundation.background\n" +
                            "import androidx.compose.foundation.gestures.scrollBy\n" +
                            "import androidx.compose.foundation.layout.*\n" +
                            "import androidx.compose.foundation.lazy.LazyColumn\n" +
                            "import androidx.compose.foundation.lazy.rememberLazyListState\n" +
                            "import androidx.compose.foundation.rememberScrollState\n" +
                            "import androidx.compose.foundation.verticalScroll\n" +
                            "import androidx.compose.material.Text\n" +
                            "import androidx.compose.runtime.*\n" +
                            "import androidx.compose.ui.Alignment\n" +
                            "import androidx.compose.ui.Modifier\n" +
                            "import androidx.compose.ui.graphics.Color\n" +
                            "import androidx.compose.ui.unit.TextUnit\n" +
                            "import androidx.compose.ui.unit.dp\n" +
                            "import androidx.compose.ui.unit.sp\n" +
                            "import com.mcxross.cohesive.mellow.Button\n" +
                            "import com.mcxross.cohesive.mellow.VerticalScrollbar\n" +
                            "import kotlinx.coroutines.flow.distinctUntilChanged"
                )

                Text(
                    "import androidx.compose.desktop.ui.tooling.preview.Preview\n" +
                            "import androidx.compose.foundation.ExperimentalFoundationApi\n" +
                            "import androidx.compose.foundation.background\n" +
                            "import androidx.compose.foundation.gestures.scrollBy\n" +
                            "import androidx.compose.foundation.layout.*\n" +
                            "import androidx.compose.foundation.lazy.LazyColumn\n" +
                            "import androidx.compose.foundation.lazy.rememberLazyListState\n" +
                            "import androidx.compose.foundation.rememberScrollState\n" +
                            "import androidx.compose.foundation.verticalScroll\n" +
                            "import androidx.compose.material.Text\n" +
                            "import androidx.compose.runtime.*\n" +
                            "import androidx.compose.ui.Alignment\n" +
                            "import androidx.compose.ui.Modifier\n" +
                            "import androidx.compose.ui.graphics.Color\n" +
                            "import androidx.compose.ui.unit.TextUnit\n" +
                            "import androidx.compose.ui.unit.dp\n" +
                            "import androidx.compose.ui.unit.sp\n" +
                            "import com.mcxross.cohesive.mellow.Button\n" +
                            "import com.mcxross.cohesive.mellow.VerticalScrollbar\n" +
                            "import kotlinx.coroutines.flow.distinctUntilChanged"
                )

                Text(
                    "import androidx.compose.desktop.ui.tooling.preview.Preview\n" +
                            "import androidx.compose.foundation.ExperimentalFoundationApi\n" +
                            "import androidx.compose.foundation.background\n" +
                            "import androidx.compose.foundation.gestures.scrollBy\n" +
                            "import androidx.compose.foundation.layout.*\n" +
                            "import androidx.compose.foundation.lazy.LazyColumn\n" +
                            "import androidx.compose.foundation.lazy.rememberLazyListState\n" +
                            "import androidx.compose.foundation.rememberScrollState\n" +
                            "import androidx.compose.foundation.verticalScroll\n" +
                            "import androidx.compose.material.Text\n" +
                            "import androidx.compose.runtime.*\n" +
                            "import androidx.compose.ui.Alignment\n" +
                            "import androidx.compose.ui.Modifier\n" +
                            "import androidx.compose.ui.graphics.Color\n" +
                            "import androidx.compose.ui.unit.TextUnit\n" +
                            "import androidx.compose.ui.unit.dp\n" +
                            "import androidx.compose.ui.unit.sp\n" +
                            "import com.mcxross.cohesive.mellow.Button\n" +
                            "import com.mcxross.cohesive.mellow.VerticalScrollbar\n" +
                            "import kotlinx.coroutines.flow.distinctUntilChanged"
                )

                Text(
                    "import androidx.compose.desktop.ui.tooling.preview.Preview\n" +
                            "import androidx.compose.foundation.ExperimentalFoundationApi\n" +
                            "import androidx.compose.foundation.background\n" +
                            "import androidx.compose.foundation.gestures.scrollBy\n" +
                            "import androidx.compose.foundation.layout.*\n" +
                            "import androidx.compose.foundation.lazy.LazyColumn\n" +
                            "import androidx.compose.foundation.lazy.rememberLazyListState\n" +
                            "import androidx.compose.foundation.rememberScrollState\n" +
                            "import androidx.compose.foundation.verticalScroll\n" +
                            "import androidx.compose.material.Text\n" +
                            "import androidx.compose.runtime.*\n" +
                            "import androidx.compose.ui.Alignment\n" +
                            "import androidx.compose.ui.Modifier\n" +
                            "import androidx.compose.ui.graphics.Color\n" +
                            "import androidx.compose.ui.unit.TextUnit\n" +
                            "import androidx.compose.ui.unit.dp\n" +
                            "import androidx.compose.ui.unit.sp\n" +
                            "import com.mcxross.cohesive.mellow.Button\n" +
                            "import com.mcxross.cohesive.mellow.VerticalScrollbar\n" +
                            "import kotlinx.coroutines.flow.distinctUntilChanged"
                )

                Text(
                    "import androidx.compose.desktop.ui.tooling.preview.Preview\n" +
                            "import androidx.compose.foundation.ExperimentalFoundationApi\n" +
                            "import androidx.compose.foundation.background\n" +
                            "import androidx.compose.foundation.gestures.scrollBy\n" +
                            "import androidx.compose.foundation.layout.*\n" +
                            "import androidx.compose.foundation.lazy.LazyColumn\n" +
                            "import androidx.compose.foundation.lazy.rememberLazyListState\n" +
                            "import androidx.compose.foundation.rememberScrollState\n" +
                            "import androidx.compose.foundation.verticalScroll\n" +
                            "import androidx.compose.material.Text\n" +
                            "import androidx.compose.runtime.*\n" +
                            "import androidx.compose.ui.Alignment\n" +
                            "import androidx.compose.ui.Modifier\n" +
                            "import androidx.compose.ui.graphics.Color\n" +
                            "import androidx.compose.ui.unit.TextUnit\n" +
                            "import androidx.compose.ui.unit.dp\n" +
                            "import androidx.compose.ui.unit.sp\n" +
                            "import com.mcxross.cohesive.mellow.Button\n" +
                            "import com.mcxross.cohesive.mellow.VerticalScrollbar\n" +
                            "import kotlinx.coroutines.flow.distinctUntilChanged"
                )
            }

        }

        if (lazyListScrollState.isScrollInProgress) {
            //This is actually a listener switch
        } else {
            coroutineScope.launch {
                textFieldScrollState.scrollTo(scrollbarAdapter.scrollOffset.toInt())
            }
        }

        VerticalScrollbar(
            scrollbarAdapter = scrollbarAdapter,
            modifier = Modifier.padding(end = 5.dp).align(Alignment.CenterEnd),
        )

    }
}
