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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

var expanded: Boolean by mutableStateOf(false)

class CMenuItem(
    private var icon: Painter?,
    private var text: String,
    private var children: List<CMenuItem> = emptyList()
) {

    fun getMenuIcon(): Painter? {
        return icon
    }

    fun getMenuText(): String {
        return text
    }

    fun getMenuChildren(): List<CMenuItem> {
        return children
    }

    fun hasChildren(): Boolean {
        return children.isNotEmpty()
    }

}

@Composable
fun CMenu(suggestions: List<CMenuItem>) {

    Box {

        WindowButton("listMenu_dark.svg", contentDescription = "Action List", width = 40.dp) {
            expanded = !expanded
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.border(border = BorderStroke(2.dp, Color(0xFF4D5051)), shape = RectangleShape)
                .width(200.dp)
        ) {
            suggestions.forEach { item ->
                CMenuItemView(item)
            }
        }

    }
}

@Composable
private fun CMenuItemView(item: CMenuItem) {
    DropdownMenuItem(
        contentPadding = PaddingValues(start = 10.dp, top = 0.dp, end = 0.dp, bottom = 0.dp),
        modifier = Modifier.height(24.dp),
        onClick = {

            expanded = false

        }) {
        Box(modifier = Modifier.fillMaxWidth().align(Alignment.CenterVertically)) {
            Row(modifier = Modifier.align(Alignment.CenterStart), horizontalArrangement = Arrangement.spacedBy(5.dp)) {

                if (item.getMenuIcon() !== null) {
                    Icon(
                        item.getMenuIcon()!!,
                        contentDescription = null,
                        modifier = Modifier.height(13.dp).align(Alignment.CenterVertically)
                    )
                }
                Text(
                    text = item.getMenuText(),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = if (item.getMenuIcon() !== null) 0.dp else 17.dp)
                        .align(Alignment.CenterVertically)
                )
            }
            if (item.hasChildren()) {
                Icon(
                    painterResource("arrowExpand_dark.svg"),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterEnd).padding(end = 10.dp).height(13.dp)
                )
            }
        }

    }
}