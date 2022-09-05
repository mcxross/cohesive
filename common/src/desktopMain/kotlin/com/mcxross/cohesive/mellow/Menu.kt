package com.mcxross.cohesive.mellow

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mcxross.cohesive.common.ds.tree.TreeNode
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

var expanded: Boolean by mutableStateOf(false)
var expandNest: Boolean by mutableStateOf(false)
var currentIndex: Int by mutableStateOf(-1)
var depth: Int by mutableStateOf(1)

class ExpandableMenu(val treeNode: TreeNode<CMenuItem>, val level: Int) {
    var children: List<ExpandableMenu> by mutableStateOf(emptyList())
    val canExpand: Boolean get() = treeNode.children.isNotEmpty()

    fun toggleExpanded() {
        children = if (children.isEmpty()) {
            treeNode.children.map { ExpandableMenu(it, level + 1) }
        } else {
            emptyList()
        }
    }
}

class MenuTree(treeNode: TreeNode<CMenuItem>) {
    private val expandableRoot = ExpandableMenu(treeNode, 0).apply {
        toggleExpanded()
    }

    val items: List<Item> get() = expandableRoot.toItems()

    inner class Item constructor(
        private val expandableMenu: ExpandableMenu
    ) {
        val name: String get() = expandableMenu.treeNode.value.text

        val level: Int get() = expandableMenu.level

        val icon: Painter? get() = expandableMenu.treeNode.value.icon

        val menuInterface: MenuInterface? get() = expandableMenu.treeNode.value.menuInterface

        val type: ItemType
            get() = if (expandableMenu.treeNode.children.isNotEmpty()) {
                ItemType.Nest(isExpanded = expandableMenu.children.isNotEmpty(), canExpand = expandableMenu.canExpand)
            } else {
                ItemType.Plain()
            }

        fun open() = when (type) {
            is ItemType.Nest -> expandableMenu.toggleExpanded()
            is ItemType.Plain -> Unit
        }
    }

    sealed class ItemType {
        class Nest(val isExpanded: Boolean, val canExpand: Boolean) : ItemType()
        class Plain() : ItemType()
    }

    private fun ExpandableMenu.toItems(): List<Item> {
        fun ExpandableMenu.addTo(list: MutableList<Item>) {
            list.add(Item(this))
            for (child in children) {
                child.addTo(list)
            }
        }

        val list = mutableListOf<Item>()
        addTo(list)
        return list
    }

}

interface MenuInterface {
    fun onClick()
    fun onHover(onEnter: Boolean)
}

class CMenuItem(
    var icon: Painter? = null,
    var text: String,
    val menuInterface: MenuInterface? = null,
)

@Composable
fun Menu(model: MenuTree, onClicked: () -> Unit) {
    fun onClick() {
        onClicked()
        expanded = !expanded
    }
    Box {
        WindowButton(
            onClick = { onClick() },
            icon = painterResource("listMenu_dark.svg"),
            contentDescription = "Action List",
            width = 40.dp
        )
        DropDown(model)
    }
}

@Composable
private fun DropDown(model: MenuTree, width: Dp = 200.dp) {

    var offSetX = 0.dp

    for (i in 1..depth) {

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.border(border = BorderStroke(2.dp, Color(0xFF4D5051)), shape = RectangleShape)
                .width(width = width),
            offset = DpOffset(offSetX, 0.dp),
        ) {
            model.items.forEachIndexed { index, item ->
                if (item.name.isNotEmpty() && item.level == i) {
                    MenuItemView(index, item)
                }
            }

        }

        offSetX += 200.dp

    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun MenuItemView(index: Int, item: MenuTree.Item) {
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

                scope.launch {
                    delay(3000)
                    isPerformingOnEnterTask = false
                }

                if (!isPerformingOnEnterTask) {
                    when (item.type) {
                        is MenuTree.ItemType.Nest -> {
                            depth += 1
                            item.open()
                        }

                        is MenuTree.ItemType.Plain -> {

                        }
                    }
                }

                true
            },
            onExit = {

                scope.launch {
                    delay(3000)
                    isPerformingOnExitTask = false
                }

                if (!isPerformingOnExitTask) {
                    when (item.type) {
                        is MenuTree.ItemType.Nest -> {
                            depth -= 1
                            item.open()
                        }

                        is MenuTree.ItemType.Plain -> {}
                    }
                }

                true
            }
        ),
        onClick = {
            expanded = false
            item.menuInterface?.onClick()
        },
    ) {
        Box(modifier = Modifier.fillMaxWidth().align(Alignment.CenterVertically)) {
            Row(modifier = Modifier.align(Alignment.CenterStart), horizontalArrangement = Arrangement.spacedBy(5.dp)) {

                if (item.icon !== null) {
                    Icon(
                        item.icon!!,
                        contentDescription = null,
                        modifier = Modifier.height(13.dp).align(Alignment.CenterVertically)
                    )
                }
                Text(
                    text = item.name,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = if (item.icon !== null) 0.dp else 17.dp)
                        .align(Alignment.CenterVertically),
                    maxLines = 1
                )
            }
            when (item.type) {
                is MenuTree.ItemType.Nest -> {
                    Icon(
                        painterResource("arrowExpand_dark.svg"),
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.CenterEnd).padding(end = 10.dp).height(13.dp)
                    )
                }

                is MenuTree.ItemType.Plain -> {


                }
            }

        }

    }
}