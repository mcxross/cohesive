@file:JvmName("MellowEditor")

package com.mcxross.cohesive.mellow

import androidx.compose.foundation.ScrollbarAdapter
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mcxross.cohesive.mellow.foundation.text.CoreTextField
import kotlinx.coroutines.launch

@Composable
internal actual fun Lines(
  textLines: TextLines,
  fontSize: TextUnit,
) =
  with(LocalDensity.current) {
    val maxNum =
      remember(textLines.size) { (1..textLines.size).joinToString(separator = "") { "9" } }

    Box(
      modifier = Modifier.fillMaxSize(),
    ) {
      // Scroll states
      val lazyListScrollState = rememberLazyListState()
      val textFieldVerticalScrollState = rememberScrollState(0)
      val textFieldHorizontalScrollState = rememberScrollState(0)
      // We need raw values to calculate the scroll position
      val scrollbarAdapter by remember { mutableStateOf(ScrollbarAdapter(lazyListScrollState)) }

      // We need to launch a coroutine to update the scroll position
      val coroutineScope = rememberCoroutineScope()

      Row(
        modifier = Modifier.fillMaxSize()
      ) {

        Row(
          modifier = Modifier.fillMaxHeight(),
        ) {
          LazyColumn(
            modifier = Modifier
              .fillMaxHeight()
              .width(81.dp),
            state = lazyListScrollState,
            contentPadding = PaddingValues(top = 2.dp),
          ) {
            items(textLines.size) { index ->
              LineNumber(
                modifier = Modifier.height(20.dp),
                maxNum = maxNum,
                line = index + 1,
                fontSize = fontSize,
              )
            }
          }

          Divider(
            modifier = Modifier.width(1.dp).fillMaxHeight(),
          )
        }

        Box(
          modifier = Modifier.fillMaxSize(),
        ) {

        }

      }



      /*  Row(
        modifier = Modifier.padding(0.dp).fillMaxSize(),
      ) {
        //LineNumber numbers' section


        //Editor section
        Box(
          modifier = Modifier
            .verticalScroll(textFieldVerticalScrollState).fillMaxSize(),
        ) {

          */
      /*TextField(
        text = textLines.text.value,
        isCode = textLines.isCode,
        fontSize = fontSize,
        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
      )

      HorizontalScrollbar(
        scrollState = textFieldHorizontalScrollState,
        modifier = Modifier.align(Alignment.BottomStart),
      )*/
      /*

        }

      }*/

      // This is actually a listener switch
      if (lazyListScrollState.isScrollInProgress) {
        //
      } else {
        coroutineScope.launch {
          textFieldVerticalScrollState.scrollTo(scrollbarAdapter.scrollOffset.toInt())
        }
      }

      VerticalScrollbar(
        modifier = Modifier.padding(end = 5.dp).align(Alignment.CenterEnd),
        scrollbarAdapter = scrollbarAdapter,
      )
    }
  }

@Composable
actual fun TextField(
  text: String,
  isCode: Boolean,
  modifier: Modifier,
  fontSize: TextUnit,
) {

  var textFieldValue by remember {
    mutableStateOf(
      TextFieldValue(
        annotatedString =
          highlight(
            text = text,
            isCode = isCode,
          ),
      ),
    )
  }
  CoreTextField(
    value = textFieldValue,
    onValueChange = { textFieldValue = it.copy(annotatedString = codeString(it.text)) },
    modifier = modifier,
    textStyle =
      TextStyle(
        fontFamily = Fonts.jetbrainsMono(),
        fontSize = fontSize,
        lineHeight = 28.sp,
      ),
    cursorBrush = SolidColor(MaterialTheme.colors.onBackground),
    softWrap = false,
  )
}
