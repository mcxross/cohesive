package com.mcxross.cohesive.mellow

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TextSnippet
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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FileTreeTab(
  text: String,
) = Surface {
  Row(
    modifier = Modifier.padding(8.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Text(
      text = text,
      color = LocalContentColor.current.copy(alpha = 0.60f),
      fontSize = 12.sp,
      modifier = Modifier.padding(horizontal = 4.dp),
    )
  }
}

@Composable
fun FileTree(
  model: FileTreeModel,
  onItemClick: (File) -> Unit = {},
) =
  Surface(
    modifier = Modifier.fillMaxSize().padding(end = 5.dp),
  ) {
    val activePath = remember { mutableStateOf("") }
    with(LocalDensity.current) {
      Box {
        val horizontalScrollState = rememberScrollState(0)
        val verticalScrollState = rememberLazyListState()
        LazyColumn(
          modifier = Modifier.matchParentSize().horizontalScroll(horizontalScrollState),
          state = verticalScrollState,
          contentPadding = PaddingValues(bottom = 8.dp),
        ) {
          items(count = model.items.size) { it ->
            FileTreeItem(
              text = model.items[it].file.absolutePath,
              modifier = Modifier.height(height = 14.sp.toDp() * 1.5f),
              activePath = activePath,
              fontSize = 14.sp,
              model = model.items[it],
            ) {
              activePath.value = it.absolutePath
              onItemClick(it)
            }
          }
        }

        HorizontalScrollbar(
          modifier = Modifier.align(Alignment.BottomCenter),
          scrollState = horizontalScrollState,
        )

        VerticalScrollbar(
          modifier = Modifier.align(Alignment.CenterEnd),
          scrollState = verticalScrollState,
        )
      }
    }
  }

@Composable
private fun FileTreeItem(
  text: String,
  modifier: Modifier,
  activePath: MutableState<String>,
  fontSize: TextUnit,
  model: FileTreeModel.Item,
  onClick: (File) -> Unit,
) {

  val interactionSource = remember { MutableInteractionSource() }

  Row(
    modifier =
      modifier
        .combinedClickableNoInteraction(
          interactionSource = interactionSource,
          indication = null,
          onDoubleClick = { model.open() },
        ) {
          onClick(model.file)
        }
        .padding(start = 24.dp * model.level)
        .background(
          if (activePath.value == "") {
            MaterialTheme.colors.surface
          } else {
            if (activePath.value == text) MaterialTheme.colors.primaryVariant
            else MaterialTheme.colors.surface
          },
        ),
  ) {
    FileItemIcon(Modifier.align(Alignment.CenterVertically), model)
    Text(
      text = model.name,
      modifier = Modifier.align(Alignment.CenterVertically).clipToBounds(),
      softWrap = true,
      fontSize = fontSize,
      overflow = TextOverflow.Ellipsis,
      maxLines = 1,
    )
  }
}

@Composable
private fun FileItemIcon(
  modifier: Modifier,
  model: FileTreeModel.Item,
) =
  Box(modifier.size(24.dp).padding(4.dp)) {
    when (val type = model.type) {
      is FileTreeModel.ItemType.Folder ->
        when {
          !type.canExpand -> Unit
          type.isExpanded ->
            Icon(
              Icons.Default.KeyboardArrowDown,
              contentDescription = null,
              tint = LocalContentColor.current,
              modifier = Modifier.clickable { model.open() },
            )
          else ->
            Icon(
              Icons.Default.KeyboardArrowRight,
              contentDescription = null,
              tint = LocalContentColor.current,
              modifier = Modifier.clickable { model.open() },
            )
        }
      is FileTreeModel.ItemType.File ->
        when (type.ext) {
          "kt" ->
            Icon(
              imageVector = Icons.Default.Code,
              contentDescription = null,
              tint = Color(0xFF3E86A0),
            )
          "xml" ->
            Icon(
              imageVector = Icons.Default.Code,
              contentDescription = null,
              tint = Color(0xFFC19C5F),
            )
          "txt" ->
            Icon(
              imageVector = Icons.Default.Description,
              contentDescription = null,
              tint = Color(0xFF87939A),
            )
          "md" ->
            Icon(
              imageVector = Icons.Default.Description,
              contentDescription = null,
              tint = Color(0xFF87939A),
            )
          "gitignore" ->
            Icon(
              imageVector = Icons.Default.BrokenImage,
              contentDescription = null,
              tint = Color(0xFF87939A),
            )
          "gradle" ->
            Icon(
              imageVector = Icons.Default.Build,
              contentDescription = null,
              tint = Color(0xFF87939A),
            )
          "kts" ->
            Icon(
              imageVector = Icons.Default.Build,
              contentDescription = null,
              tint = Color(0xFF3E86A0),
            )
          "properties" ->
            Icon(
              imageVector = Icons.Default.Settings,
              contentDescription = null,
              tint = Color(0xFF62B543),
            )
          "bat" ->
            Icon(
              imageVector = Icons.Default.Launch,
              contentDescription = null,
              tint = Color(0xFF87939A),
            )
          else ->
            Icon(
              imageVector = Icons.Default.TextSnippet,
              contentDescription = null,
              tint = Color(0xFF87939A),
            )
        }
    }
  }
