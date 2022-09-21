package com.mcxross.cohesive.mellow

import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.debugInspectorInfo

/* Original author, tunjid*/
interface DropTarget {
    fun onDragStarted(uris: List<Uri>, position: Offset): Boolean
    fun onDragEntered()
    fun onDragMoved(position: Offset) {}
    fun onDragExited()
    fun onDropped(uris: List<Uri>, position: Offset): Boolean
    fun onDragEnded()
}

interface DropTargetModifier : DropTarget, Modifier.Element

internal fun dropTargetModifier(): DropTargetModifier = DropTargetContainer(
    onDragStarted = { _, _ -> DragAction.Reject }
)

expect class PlatformDropTargetModifier : DropTargetModifier

fun Modifier.dropTarget(
    onDragStarted: (uris: List<Uri>, Offset) -> Boolean,
    onDragEntered: () -> Unit = { },
    onDragMoved: (position: Offset) -> Unit = {},
    onDragExited: () -> Unit = { },
    onDropped: (uris: List<Uri>, position: Offset) -> Boolean,
    onDragEnded: () -> Unit = {},
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "dropTarget"
        properties["onDragStarted"] = onDragStarted
    },
    factory = {
        val node = remember {
            DropTargetContainer { uris, offset ->
                when (onDragStarted(uris, offset)) {
                    false -> DragAction.Reject
                    true -> DragAction.Accept(
                        object : DropTarget {
                            override fun onDragStarted(uris: List<Uri>, position: Offset): Boolean = onDragStarted(
                                uris,
                                position
                            )

                            override fun onDragEntered() = onDragEntered()

                            override fun onDragMoved(position: Offset) = onDragMoved(position)

                            override fun onDragExited() = onDragExited()

                            override fun onDropped(uris: List<Uri>, position: Offset): Boolean = onDropped(
                                uris,
                                position
                            )

                            override fun onDragEnded() = onDragEnded()
                        }
                    )
                }
            }
        }
        this.then(node)
    })

