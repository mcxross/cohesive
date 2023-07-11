package xyz.mcxross.cohesive.designsystem.mellow

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
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import xyz.mcxross.cohesive.common.algo.ds.tree.TreeNode
import kotlinx.coroutines.launch

interface MenuInterface {
  fun onClick()
  fun onHover(onEnter: Boolean)
}

data class MenuContainer(
  val icon: Painter,
  val contentDescription: String,
  val onClick: () -> Unit = {},
  val menuTree: TreeNode<MenuItem>
)

data class SubMenuContainer(val menuItems: List<MenuItem>)

data class MenuItem(
  var icon: Painter? = null,
  var text: String,
  val menuInterface: MenuInterface? = null,
  private val subMenuContainer: SubMenuContainer? = null,
  var position: Int = -1,
  var isHovered: Boolean = false,
  var onHover: (Boolean) -> Unit = {}
)

class MenuState {
  var menuItem by mutableStateOf<MenuItem?>(null)
  var isPerformingOnEnterTask by mutableStateOf(false)
  var isPerformingOnExitTask by mutableStateOf(false)
  var expanded: Boolean by mutableStateOf(false)
  var node: TreeNode<MenuItem>? by mutableStateOf(null)
  var menuTree: TreeNode<MenuItem>? by mutableStateOf(null)

  fun toggleMenu() {
    expanded = !expanded
  }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun MenuItemView(node: TreeNode<MenuItem>, menuState: MenuState) {
  val menuItem = node.value
  DropdownMenuItem(
    contentPadding = PaddingValues(start = 10.dp, top = 0.dp, end = 0.dp, bottom = 0.dp),
    modifier = Modifier.height(24.dp).onPointerEvent(
      PointerEventType.Move,
    ) {}.onPointerEvent(PointerEventType.Enter) {
      menuItem.onHover(true)
    }.onPointerEvent(PointerEventType.Exit) {
      menuItem.onHover(false)
    },
    onClick = {
      menuState.toggleMenu()
      menuItem.menuInterface?.onClick()
    },
  ) {

    val coroutineScope = rememberCoroutineScope()

    menuItem.onHover = { onHover ->
      menuState.node = node
      menuState.menuItem = menuItem
      menuState.menuItem?.isHovered = onHover
      coroutineScope.launch {
        menuState.isPerformingOnEnterTask = true
        menuState.isPerformingOnExitTask = true
      }
    }

    Box(
      modifier = Modifier.fillMaxWidth().align(Alignment.CenterVertically),
    ) {
      Row(
        modifier = Modifier.align(Alignment.CenterStart),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
      ) {

        if (menuItem.icon !== null) {
          Icon(
            painter = menuItem.icon!!,
            contentDescription = null,
            modifier = Modifier.height(13.dp).align(Alignment.CenterVertically),
          )
        }
        Text(
          text = menuItem.text,
          fontSize = 12.sp,
          modifier = Modifier.padding(start = if (menuItem.icon !== null) 0.dp else 17.dp)
            .align(Alignment.CenterVertically),
          maxLines = 1,
        )
      }
      if (node.nodeCount() > 0)
        Icon(
          painterResource("arrowExpand_dark.svg"),
          contentDescription = null,
          modifier = Modifier.align(Alignment.CenterEnd).padding(end = 10.dp).height(13.dp),
        )
    }

  }

}

@Composable
fun MenuCard(
  content: @Composable () -> Unit,
) = Card(
  modifier = Modifier.width(200.dp),
  shape = RectangleShape,
) {
  Column {
    content()
  }
}

@Composable
fun SubMenu(menuState: MenuState) = Row {
  if (menuState.node!!.nodeCount() > 0) {
    for (i in 0 until menuState.node!!.depth()) {
      MenuCard {
        menuState.menuTree?.filter { it.depth() == i + 2 }
          ?.forEach { node ->
            MenuItemView(node, menuState)
          }
      }
    }
  } else {
    for (i in 0 until menuState.node!!.depth() - 1) {
      MenuCard {
        menuState.menuTree?.filter { it.depth() == i + 2 }
          ?.forEach { node ->
            MenuItemView(node, menuState)
          }
      }
    }
  }
}


@Composable
fun Menu(index: Int, menuContainer: MenuContainer, onClick: () -> Unit) = Box {

  val menuState = remember { MenuState() }
  menuState.menuTree = menuContainer.menuTree

  val node = menuState.node

  WindowButton(
    onClick = {
      onClick()
      menuState.toggleMenu()
    },
    icon = menuContainer.icon,
    contentDescription = menuContainer.contentDescription,
    width = 40.dp,
  )
  if (menuState.expanded) {
    Popup(
      onDismissRequest = { menuState.toggleMenu() },
      alignment = Alignment.TopStart,
      offset = IntOffset(0, 47),
    ) {
      Row {
        MenuCard {
          menuContainer.menuTree.filter { it.depth() == 1 }.forEach {
            MenuItemView(it, menuState)
          }
        }
        if (node != null) {
          if (node.value.isHovered) {
            SubMenu(menuState = menuState)
          }
        }
      }
    }
  }
}
