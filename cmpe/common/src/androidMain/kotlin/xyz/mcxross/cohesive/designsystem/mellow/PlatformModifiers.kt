package xyz.mcxross.cohesive.designsystem.mellow

import android.view.DragEvent
import android.view.View
import androidx.compose.ui.geometry.Offset

actual class PlatformDropTargetModifier(
  view: View,
) : DropTargetModifier by dropTargetModifier() {
  init {
    view.setOnDragListener(dragListener(this))
  }
}

fun dragListener(
  dropTargetModifier: DropTargetModifier,
): View.OnDragListener = View.OnDragListener { _, event ->
  when (event.action) {
    DragEvent.ACTION_DRAG_STARTED -> {
      dropTargetModifier.onDragStarted(
        uris = listOf(),
        position = Offset(event.x, event.y),
      )
      true
    }

    DragEvent.ACTION_DRAG_ENTERED -> {
      dropTargetModifier.onDragEntered()
      true
    }

    DragEvent.ACTION_DRAG_LOCATION -> {
      dropTargetModifier.onDragMoved(Offset(event.x, event.y))
      true
    }

    DragEvent.ACTION_DRAG_EXITED -> {
      dropTargetModifier.onDragExited()
      true
    }

    DragEvent.ACTION_DROP -> {
      dropTargetModifier.onDropped(
        uris = event.clipItemUris(),
        position = Offset(event.x, event.y),
      )
    }

    DragEvent.ACTION_DRAG_ENDED -> {
      dropTargetModifier.onDragEnded()
      true
    }

    else -> error("Invalid action: ${event.action}")
  }
}

private fun DragEvent.clipItemUris(): List<Uri> = with(clipData) {
  /*0.until(itemCount).map { itemIndex ->
      with(description) {
          0.until(mimeTypeCount).map { mimeTypeIndex ->
              ClipItemUri(
                  item = getItemAt(itemIndex),
                  mimeType = getMimeType(mimeTypeIndex)
              )
          }
      }
  }
      .flatten()*/
  return@with emptyList()
}
