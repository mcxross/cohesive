package com.mcxross.cohesive.mellow

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import com.mcxross.cohesive.common.frontend.model.Local

object Toast {
  var model: ToastModel by mutableStateOf(ToastModel())
  fun message(title: String = "", message: String) {
    model.toast(title = title, message = message)
  }
}

@Composable
fun WindowScaffold(
  modifier: Modifier = Modifier,
  topBar: @Composable () -> Unit = {},
  bottomBar: @Composable () -> Unit = {},
  enableDnD: Boolean = true,
  onDragStarted: (uris: List<Uri>, Offset) -> Boolean,
  onDragEntered: () -> Unit = { },
  onDragMoved: (position: Offset) -> Unit = {},
  onDragExited: () -> Unit = { },
  onDropped: (uris: List<Uri>, position: Offset) -> Boolean,
  onDragEnded: () -> Unit = {},
  content: @Composable (BoxScope.() -> Unit) = {},
) {
  DisableSelection {
    MellowTheme.Theme {
      Surface(
        modifier = modifier.fillMaxSize()
          .then(Local.LocalContext.current.platformDropTargetModifier!!),
        contentColor = contentColorFor(MaterialTheme.colors.surface),
      ) {
        Column {
          topBar()
          Box(modifier = Modifier.weight(1f)) {
            content()
            if (enableDnD) {
              Box(
                modifier = Modifier.fillMaxSize().dropTarget(
                  onDragStarted = { uris, position -> onDragStarted(uris, position) },
                  onDragEntered = { onDragEntered() },
                  onDragMoved = { position -> onDragMoved(position) },
                  onDragExited = { onDragExited() },
                  onDropped = { uris, position -> onDropped(uris, position) },
                  onDragEnded = { onDragEnded() },
                ),
              )
            }

          }
          Divider()
          StatusBar { bottomBar() }
        }

        Toast(model = Toast.model)
      }
    }
  }
}
