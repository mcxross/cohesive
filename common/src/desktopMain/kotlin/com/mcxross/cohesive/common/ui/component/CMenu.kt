package com.mcxross.cohesive.common.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

var expanded: Boolean by mutableStateOf(false)
var expandNest: Boolean by mutableStateOf(false)
var currentIndex: Int by mutableStateOf(-1)
var depth: Int by mutableStateOf(1)

class ExpandleMenu(val cMenuItem: CMenuItem, val level: Int) {
    var children: List<ExpandleMenu> by mutableStateOf(emptyList())
    val canExpand: Boolean get() = cMenuItem.hasChildren()

    fun toggleExpanded() {
        children = if (children.isEmpty()) {
            cMenuItem.getMenuChildren()
                .map { ExpandleMenu(it, level + 1) }
        } else {
            emptyList()
        }
    }
}

class MenuTree(root: CMenuItem) {
    private val expandableRoot = ExpandleMenu(root, 0).apply {
        toggleExpanded()
    }

    val items: List<Item> get() = expandableRoot.toItems()

    inner class Item constructor(
        private val expandleMenu: ExpandleMenu
    ) {
        val name: String get() = expandleMenu.cMenuItem.getMenuText()

        val level: Int get() = expandleMenu.level

        val icon: Painter? get() = expandleMenu.cMenuItem.getMenuIcon()

        val cMenuInterface: CMenuInterface? get() = expandleMenu.cMenuItem.getMenuInterface()

        val type: ItemType
            get() = if (expandleMenu.cMenuItem.hasChildren()) {
                ItemType.Nest(isExpanded = expandleMenu.children.isNotEmpty(), canExpand = expandleMenu.canExpand)
            } else {
                ItemType.Plain()
            }

        fun open() = when (type) {
            is ItemType.Nest -> expandleMenu.toggleExpanded()
            is ItemType.Plain -> Unit
        }
    }

    sealed class ItemType {
        class Nest(val isExpanded: Boolean, val canExpand: Boolean) : ItemType()
        class Plain() : ItemType()
    }

    private fun ExpandleMenu.toItems(): List<Item> {
        fun ExpandleMenu.addTo(list: MutableList<Item>) {
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

interface CMenuInterface {
    fun onClick()
    fun onHover(onEnter: Boolean)
}

class CMenuItem(
    private var icon: Painter?,
    private var text: String,
    private var children: List<CMenuItem> = emptyList(),
    private val cMenuInterface: CMenuInterface? = null,
) {

    fun getMenuIcon(): Painter? {
        return icon
    }

    fun getMenuText(): String {
        return text
    }

    fun addMenu(child: CMenuItem): CMenuItem {
        children = children + child
        return this
    }

    fun getMenuChildren(): List<CMenuItem> {
        return children
    }

    fun hasChildren(): Boolean {
        return children.isNotEmpty()
    }


    fun onClick(): Unit {
        cMenuInterface?.onClick()
    }

    fun onHover(onEnter: Boolean) {
        //cMenuInterface?.onHover(onEnter)
    }

    fun getMenuInterface(): CMenuInterface? {
        return cMenuInterface
    }

}

@Composable
fun CMenu(model: MenuTree) {
    Box {
        WindowButton("listMenu_dark.svg", contentDescription = "Action List", width = 40.dp) {

            expanded = !expanded
        }

        DropDown(model)
    }
}

@Composable
private fun DropDown(model: MenuTree) {

    DD(model)

}

@Composable
private fun DD(model: MenuTree, width: Dp = 200.dp) {

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
                    CMenuItemView(index, item)
                }
            }

        }

        offSetX += 200.dp

    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun CMenuItemView(index: Int, item: MenuTree.Item) {
    DropdownMenuItem(
        contentPadding = PaddingValues(start = 10.dp, top = 0.dp, end = 0.dp, bottom = 0.dp),
        modifier = Modifier.height(24.dp).pointerMoveFilter(
            onEnter = {

                when (item.type) {
                    is MenuTree.ItemType.Nest -> {
                        depth += 1
                        item.open()
                    }

                    is MenuTree.ItemType.Plain -> {

                    }
                }

                true
            },
            onExit = {
                when (item.type) {
                    is MenuTree.ItemType.Nest -> {
                        depth -= 1
                        item.open()
                    }

                    is MenuTree.ItemType.Plain -> {}
                }
                true
            }
        ),
        onClick = {
            expanded = false
            item.cMenuInterface?.onClick()
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