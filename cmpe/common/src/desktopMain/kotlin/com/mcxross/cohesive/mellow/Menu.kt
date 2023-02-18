package com.mcxross.cohesive.mellow

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.mcxross.cohesive.common.ds.tree.TreeNode

var expanded: Boolean by mutableStateOf(false)
var expandNest: Boolean by mutableStateOf(false)
var currentIndex: Int by mutableStateOf(-1)
var depth: Int by mutableStateOf(1)
interface MenuInterface {
  fun onClick()
  fun onHover(onEnter: Boolean)
}

data class Menu(val icon: Painter? = null, val items: List<MenuItem>)
data class SubMenu(val items: List<MenuItem>)

data class MenuItem(
  var icon: Painter? = null,
  var text: String,
  val menuInterface: MenuInterface? = null,
  val submenu: SubMenu? = null,
)

@Composable
fun MenuCard(
  content: @Composable () -> Unit,
) {
  Card(
    modifier = Modifier.width(200.dp),
    shape = RectangleShape,
  ) {
    Column {
      content()
    }
  }
}

@Composable
fun Menu(model: List<Menu>, onClicked: () -> Unit) {
  fun onClick() {
    onClicked()
    expanded = !expanded
  }
  Box {
    WindowButton(
      onClick = { onClick() },
      icon = painterResource("listMenu_dark.svg"),
      contentDescription = "Action List",
      width = 40.dp,
    )
    if (expanded) {
      Popup(
        onDismissRequest = { expanded = false },
        alignment = Alignment.TopStart,
        offset = IntOffset(0, 46),
        content = {
          Row {
            for (menu in model) {
              MenuCard {
                menu.items.forEachIndexed { index, item ->
                  MenuItemView(index, item)
                }
              }
              for (item in menu.items) {
                if (item.submenu != null) {
                  MenuCard {
                    item.submenu.items.forEachIndexed { index, item ->
                      MenuItemView(index, item)
                    }
                  }
                }
              }
            }
          }
        },
      )
    }
  }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun MenuItemView(index: Int, item: MenuItem) {
  var isPerformingOnEnterTask by remember { mutableStateOf(true) }
  var isPerformingOnExitTask by remember { mutableStateOf(true) }
  val scope = rememberCoroutineScope()
  DropdownMenuItem(
    contentPadding = PaddingValues(start = 10.dp, top = 0.dp, end = 0.dp, bottom = 0.dp),
    modifier = Modifier.height(24.dp).pointerMoveFilter(
      onMove = {
        true
      },
      onEnter = {
        true
      },
      onExit = {
        true
      },
    ),
    onClick = {
      expanded = false
      item.menuInterface?.onClick()
    },
  ) {
    Box(modifier = Modifier.fillMaxWidth().align(Alignment.CenterVertically)) {
      Row(
        modifier = Modifier.align(Alignment.CenterStart),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
      ) {

        if (item.icon !== null) {
          Icon(
            painter = item.icon!!,
            contentDescription = null,
            modifier = Modifier.height(13.dp).align(Alignment.CenterVertically),
          )
        }
        Text(
          text = item.text,
          fontSize = 12.sp,
          modifier = Modifier.padding(start = if (item.icon !== null) 0.dp else 17.dp)
            .align(Alignment.CenterVertically),
          maxLines = 1,
        )
      }
      if (item.submenu !== null) {
        Icon(
          painterResource("arrowExpand_dark.svg"),
          contentDescription = null,
          modifier = Modifier.align(Alignment.CenterEnd).padding(end = 10.dp).height(13.dp),
        )
      }

    }

  }
}
