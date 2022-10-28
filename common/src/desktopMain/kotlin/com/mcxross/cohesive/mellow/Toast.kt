package com.mcxross.cohesive.mellow

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun Toast(
  model: ToastModel,
  modifier: Modifier = Modifier,
  alignment: Alignment = Alignment.BottomEnd,
) = Box(
  modifier = modifier.fillMaxSize().padding(end = 10.dp, bottom = 15.dp),
) {
  val listState = rememberLazyListState()
  LazyColumn(
    state = listState,
    modifier = Modifier.wrapContentWidth()
      .wrapContentHeight()
      .align(alignment),
    verticalArrangement = Arrangement.spacedBy(4.dp),
  ) {
    items(model.state.items.reversed()) { item ->
      Item(
        item = item,
        model = model,
      )
    }

  }
}

@Composable
private fun Item(
  item: ToastItem,
  model: ToastModel,
) = Card(
  modifier = Modifier.width(width = 300.dp).defaultMinSize(minHeight = 60.dp),
  elevation = 5.dp,
) {
  val currentOnTimeout by rememberUpdatedState { model.onItemCloseClicked(item.id) }
  LaunchedEffect(true) {
    delay(10000)
    currentOnTimeout()
  }
  Column {
    Box(modifier = Modifier.fillMaxWidth().height(15.dp).padding(start = 5.dp)) {
      Text(
        text = item.title,
        fontSize = 12.sp,
        fontWeight = FontWeight.W300,
      )
      Icon(
        painter = painterResource("closeSmall_dark.svg"),
        contentDescription = "close",
        modifier = Modifier
          .width(15.dp)
          .fillMaxHeight()
          .align(Alignment.TopEnd).clickable { model.onItemCloseClicked(item.id) },
      )
    }
    Text(
      text = item.message,
      modifier = Modifier.padding(start = 7.dp, top = 3.dp),
      fontSize = 13.sp,
      fontWeight = FontWeight.W300,
    )
  }
}
