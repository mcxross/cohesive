package xyz.mcxross.cohesive.ui.impl.view

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import xyz.mcxross.cohesive.core.editor.EditorManager
import xyz.mcxross.cohesive.designsystem.mellow.File

@Composable
fun EditorSimple(
  file: File,
) {
  val editorManager = remember { EditorManager() }
  // onCLose recomposes with the file, not closing
  editorManager.open(file)
  if (editorManager.isActiveNonNull()) {
    Column(
      modifier = Modifier.fillMaxSize(),
    ) {
      EditorTabs(editorManager = editorManager)
      Box(modifier = Modifier.weight(1f)) {
        Crossfade(targetState = editorManager.active) {
          Editor(
            model = it!!,
          )
        }
      }
    }
  } else {
    EditorEmpty()
  }
}
