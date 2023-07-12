@file:JvmName("MellowEditor")

package xyz.mcxross.cohesive.ui.impl.view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
import xyz.mcxross.cohesive.designsystem.mellow.foundation.text.CoreTextField
import kotlinx.coroutines.launch
import xyz.mcxross.cohesive.designsystem.mellow.Fonts
import xyz.mcxross.cohesive.designsystem.mellow.TextLines

@Composable
internal actual fun EditorMap(
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

      Row(modifier = Modifier.fillMaxSize()) {
        Row(
          modifier = Modifier.fillMaxHeight(),
        ) {
          LazyColumn(
            modifier = Modifier.fillMaxHeight().width(81.dp),
            state = lazyListScrollState,
            contentPadding = PaddingValues(top = 2.dp),
          ) {
            items(textLines.size) { index ->
              LineNumber(
                modifier = Modifier.height(24.dp),
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
          modifier =
            Modifier.weight(1f)
              .horizontalScroll(state = textFieldHorizontalScrollState)
              .verticalScroll(state = textFieldVerticalScrollState)
              .padding(start = 3.dp, end = 30.dp),
        ) {
          TextField(
            text = textLines.text.value,
            onScroll = {},
            isCode = textLines.isCode,
            fontSize = fontSize,
            modifier = Modifier.fillMaxSize(),
          )
        }
      }

      xyz.mcxross.cohesive.designsystem.mellow.HorizontalScrollbar(
        scrollState = textFieldHorizontalScrollState,
        modifier = Modifier.padding(start = 90.dp, end = 15.dp).align(Alignment.BottomStart),
      )

      // This is actually a listener switch
      if (lazyListScrollState.isScrollInProgress) {
        //
      } else {
        coroutineScope.launch {
          textFieldVerticalScrollState.scrollTo(scrollbarAdapter.scrollOffset.toInt())
        }
      }

      xyz.mcxross.cohesive.designsystem.mellow.VerticalScrollbar(
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
  onScroll: (Float) -> Unit,
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
    onScroll = onScroll,
    modifier = modifier.paddingFromBaseline(top = 0.dp, bottom = 12.dp).focusable(true),
    textStyle =
      TextStyle(
        fontFamily = Fonts.jetbrainsMono(),
        fontSize = fontSize,
        lineHeight = 24.sp,
      ),
    cursorBrush = SolidColor(MaterialTheme.colors.onBackground),
    softWrap = false,
  )
}
