package com.mcxross.cohesive.common.ui.view.editor.filetree

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mcxross.cohesive.common.ui.view.editor.platform.VerticalScrollbar

val activeIndex =  mutableStateOf(0)

@Composable
fun FileTreeViewTabView() = Surface {
    Row(
        Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Files",
            color = LocalContentColor.current.copy(alpha = 0.60f),
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}

@Composable
fun FileTreeView(model: FileTree) {
    Surface(modifier = Modifier.fillMaxSize().padding(end = 3.dp)) {
        with(LocalDensity.current) {
            Box {
                val scrollState = rememberLazyListState()

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = scrollState,
                ) {
                    items(model.items.size) {

                        FileTreeItemView(it, 14.sp, 14.sp.toDp() * 1.5f, model.items[it])

                    }
                }

                VerticalScrollbar(
                    Modifier.align(Alignment.CenterEnd),
                    scrollState
                )
            }
        }
    }
}

@Composable
private fun FileTreeItemView(i: Int, fontSize: TextUnit, height: Dp, model: FileTree.Item) {

    Box(
        modifier = Modifier.fillMaxWidth()
            .wrapContentHeight()
            .height(height)
            .background(
                if (activeIndex.value == 0) {
                    MaterialTheme.colors.surface
                } else {
                    if (activeIndex.value == i) MaterialTheme.colors.primaryVariant else MaterialTheme.colors.surface
                }
            )
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
                .clickable {
                    activeIndex.value = i
                }
                .padding(start = 24.dp * model.level)
        ) {

            FileItemIcon(Modifier.align(Alignment.CenterVertically), model)
            Text(
                text = model.name,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .clipToBounds(),
                softWrap = true,
                fontSize = fontSize,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }

}

@Composable
private fun FileItemIcon(modifier: Modifier, model: FileTree.Item) = Box(modifier.size(24.dp).padding(4.dp)) {
    when (val type = model.type) {
        is FileTree.ItemType.Folder -> when {
            !type.canExpand -> Unit
            type.isExpanded -> Icon(
                Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = LocalContentColor.current,
                modifier = Modifier.clickable {
                    model.open()
                }
            )

            else -> Icon(
                Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = LocalContentColor.current,
                modifier = Modifier.clickable {
                    model.open()
                }
            )
        }

        is FileTree.ItemType.File -> when (type.ext) {
            "kt" -> Icon(Icons.Default.Code, contentDescription = null, tint = Color(0xFF3E86A0))
            "xml" -> Icon(Icons.Default.Code, contentDescription = null, tint = Color(0xFFC19C5F))
            "txt" -> Icon(Icons.Default.Description, contentDescription = null, tint = Color(0xFF87939A))
            "md" -> Icon(Icons.Default.Description, contentDescription = null, tint = Color(0xFF87939A))
            "gitignore" -> Icon(Icons.Default.BrokenImage, contentDescription = null, tint = Color(0xFF87939A))
            "gradle" -> Icon(Icons.Default.Build, contentDescription = null, tint = Color(0xFF87939A))
            "kts" -> Icon(Icons.Default.Build, contentDescription = null, tint = Color(0xFF3E86A0))
            "properties" -> Icon(Icons.Default.Settings, contentDescription = null, tint = Color(0xFF62B543))
            "bat" -> Icon(Icons.Default.Launch, contentDescription = null, tint = Color(0xFF87939A))
            else -> Icon(Icons.Default.TextSnippet, contentDescription = null, tint = Color(0xFF87939A))
        }
    }
}