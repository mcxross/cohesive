package xyz.mcxross.cohesive.mellow

import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.debugInspectorInfo

/**
 * Interface for receiving drag events.
 *
 * Original Author: [tunjid]
 *
 * Code attributed to [tunjid] as the original author.
 */
interface DropTarget {
  /**
   * Called when a drag operation has begun.
   *
   * @param uris The URIs of the dragged items.
   * @param position The position of the drag event in screen coordinates.
   * @return True if the drag operation should continue, false if it should be cancelled.
   */
  fun onDragStarted(uris: List<Uri>, position: Offset): Boolean

  /** Called when an object is dragged into the drop target. */
  fun onDragEntered()

  /**
   * Called when an object is dragged within the drop target.
   *
   * @param position The position of the drag event in screen coordinates.
   */
  fun onDragMoved(position: Offset) {}

  /** Called when an object is dragged out of the drop target. */
  fun onDragExited()

  /**
   * Called when an object is dropped on the drop target.
   *
   * @param uris The URIs of the dropped items.
   * @param position The position of the drop event in screen coordinates.
   * @return True if the drop operation was successful, false otherwise.
   */
  fun onDropped(uris: List<Uri>, position: Offset): Boolean

  /** Called when a drag operation has ended. */
  fun onDragEnded()
}

interface DropTargetModifier : DropTarget, Modifier.Element

internal fun dropTargetModifier(): DropTargetModifier =
  DropTargetContainer(
    onDragStarted = { _, _ -> DragAction.Reject },
  )

expect class PlatformDropTargetModifier : DropTargetModifier

fun Modifier.dropTarget(
  onDragStarted: (uris: List<Uri>, Offset) -> Boolean,
  onDragEntered: () -> Unit = {},
  onDragMoved: (position: Offset) -> Unit = {},
  onDragExited: () -> Unit = {},
  onDropped: (uris: List<Uri>, position: Offset) -> Boolean,
  onDragEnded: () -> Unit = {},
): Modifier =
  composed(
    inspectorInfo =
      debugInspectorInfo {
        name = "dropTarget"
        properties["onDragStarted"] = onDragStarted
      },
    factory = {
      val node = remember {
        DropTargetContainer { uris, offset ->
          when (onDragStarted(uris, offset)) {
            false -> DragAction.Reject
            true ->
              DragAction.Accept(
                object : DropTarget {
                  override fun onDragStarted(uris: List<Uri>, position: Offset): Boolean =
                    onDragStarted(
                      uris,
                      position,
                    )

                  override fun onDragEntered() = onDragEntered()

                  override fun onDragMoved(position: Offset) = onDragMoved(position)

                  override fun onDragExited() = onDragExited()

                  override fun onDropped(uris: List<Uri>, position: Offset): Boolean =
                    onDropped(
                      uris,
                      position,
                    )

                  override fun onDragEnded() = onDragEnded()
                },
              )
          }
        }
      }
      this.then(node)
    },
  )
