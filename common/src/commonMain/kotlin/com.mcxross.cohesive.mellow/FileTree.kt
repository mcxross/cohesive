package com.mcxross.cohesive.mellow

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

@Composable
fun FileTreeTab(
    text: String,
) = Surface {
    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            color = LocalContentColor.current.copy(alpha = 0.60f),
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}

@Composable
fun FileTree(
    model: FileTreeModel,
    onItemClick: (File) -> Unit = {},
) = Surface(
    modifier = Modifier.fillMaxSize().padding(end = 3.dp)
) {
    val activePath = remember { mutableStateOf("") }
    with(LocalDensity.current) {
        Box {
            val scrollState = rememberLazyListState()

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = scrollState,
            ) {
                items(count = model.items.size) { it ->

                    FileTreeItem(
                        path = model.items[it].file.absolutePath,
                        activePath = activePath,
                        fontSize = 14.sp,
                        height = 14.sp.toDp() * 1.5f,
                        model = model.items[it]
                    ) {
                        activePath.value = it.absolutePath
                        onItemClick(it)
                    }

                }
            }

            VerticalScrollbar(
                modifier = Modifier.align(Alignment.CenterEnd),
                scrollState = scrollState
            )
        }
    }
}


@Composable
private fun FileTreeItem(
    path: String,
    activePath: MutableState<String>,
    fontSize: TextUnit,
    height: Dp,
    model: FileTreeModel.Item,
    onClick: (File) -> Unit,
) {

    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = Modifier.fillMaxWidth()
            .wrapContentHeight()
            .height(height = height)
            .background(
                if (activePath.value == "") {
                    MaterialTheme.colors.surface
                } else {
                    if (activePath.value == path) MaterialTheme.colors.primaryVariant else MaterialTheme.colors.surface
                }
            )
    ) {
        Row(
            modifier = Modifier.fillMaxSize().combinedClickableNoInteraction(
                interactionSource = interactionSource,
                indication = null,
                onDoubleClick = {
                    model.open()
                }
            ) { onClick(model.file) }.padding(start = 24.dp * model.level)
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
private fun FileItemIcon(
    modifier: Modifier,
    model: FileTreeModel.Item,
) = Box(modifier.size(24.dp).padding(4.dp)) {
    when (val type = model.type) {
        is FileTreeModel.ItemType.Folder -> when {
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

        is FileTreeModel.ItemType.File -> when (type.ext) {
            "kt" -> Icon(imageVector = Icons.Default.Code, contentDescription = null, tint = Color(0xFF3E86A0))
            "xml" -> Icon(imageVector = Icons.Default.Code, contentDescription = null, tint = Color(0xFFC19C5F))
            "txt" -> Icon(imageVector = Icons.Default.Description, contentDescription = null, tint = Color(0xFF87939A))
            "md" -> Icon(imageVector = Icons.Default.Description, contentDescription = null, tint = Color(0xFF87939A))
            "gitignore" -> Icon(
                imageVector = Icons.Default.BrokenImage,
                contentDescription = null,
                tint = Color(0xFF87939A)
            )

            "gradle" -> Icon(imageVector = Icons.Default.Build, contentDescription = null, tint = Color(0xFF87939A))
            "kts" -> Icon(imageVector = Icons.Default.Build, contentDescription = null, tint = Color(0xFF3E86A0))
            "properties" -> Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = null,
                tint = Color(0xFF62B543)
            )

            "bat" -> Icon(imageVector = Icons.Default.Launch, contentDescription = null, tint = Color(0xFF87939A))
            else -> Icon(imageVector = Icons.Default.TextSnippet, contentDescription = null, tint = Color(0xFF87939A))
        }
    }
}