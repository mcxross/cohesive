package xyz.mcxross.cohesive.designsystem.mellow

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import xyz.mcxross.cohesive.project.Project

private operator fun TextUnit.minus(other: TextUnit) = (value - other.value).sp

private operator fun TextUnit.div(other: TextUnit) = value / other.value

@Composable
fun EditorComposite(
  text: String = "Project",
  project: Project,
) {

  val editorCompositeContainer = remember {
    val editorManager = EditorManager()

    EditorCompositeContainer(
      editorManager = editorManager,
      fileTreeModel = FileTreeModel(project.root) { editorManager.open(it) },
    )
  }

  val panelState = remember { PanelState() }

  val animatedSize =
    if (panelState.splitter.isResizing) {
      if (panelState.isExpanded) panelState.expandedSize else panelState.collapsedSize
    } else {
      animateDpAsState(
        if (panelState.isExpanded) panelState.expandedSize else panelState.collapsedSize,
        SpringSpec(stiffness = Spring.StiffnessLow),
      )
        .value
    }

  VerticalSplittable(
    modifier = Modifier.fillMaxSize(),
    splitterState = panelState.splitter,
    onResize = {
      panelState.expandedSize =
        (panelState.expandedSize + it).coerceAtLeast(panelState.expandedSizeMin)
    },
  ) {
    ResizablePanel(
      modifier = Modifier.width(animatedSize).fillMaxHeight(),
      state = panelState,
    ) {
      Column {
        FileTreeTab(text = text)
        FileTree(model = editorCompositeContainer.fileTreeModel)
      }
    }

    Box {
      if (editorCompositeContainer.editorManager.isActiveNonNull()) {
        Column(
          modifier = Modifier.fillMaxSize(),
        ) {
          EditorTabs(editorCompositeContainer = editorCompositeContainer, panelState = panelState)
          Box(
            modifier = Modifier.weight(1f),
          ) {
            Crossfade(targetState = editorCompositeContainer.editorManager.active) {
              it?.let { model -> Editor(model = model) }
            }
          }
        }
      } else {
        EditorEmpty(
          text = "To view file open it from the file tree",
        )
      }
    }
  }
}
