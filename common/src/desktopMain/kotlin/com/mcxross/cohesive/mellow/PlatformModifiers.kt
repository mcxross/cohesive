package com.mcxross.cohesive.mellow

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Indication
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.semantics.Role
import java.awt.datatransfer.DataFlavor
import java.awt.dnd.*
import java.awt.dnd.DropTarget
import java.io.File

/**
 * An overload of [Modifier.combinedClickable]
 *
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Modifier.combinedClickableNoInteractionDesktop(
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    indication: Indication?,
    onClickLabel: String? = null,
    role: Role? = null,
    onLongClickLabel: String? = null,
    onLongClick: (() -> Unit)? = null,
    onDoubleClick: (() -> Unit)? = null,
    onClick: () -> Unit,
) = composed(
    inspectorInfo = debugInspectorInfo {
        name = "combinedClickable"
        properties["enabled"] = enabled
        properties["onClickLabel"] = onClickLabel
        properties["role"] = role
        properties["onClick"] = onClick
        properties["onDoubleClick"] = onDoubleClick
        properties["onLongClick"] = onLongClick
        properties["onLongClickLabel"] = onLongClickLabel
    }
) {
    Modifier.combinedClickable(
        enabled = enabled,
        onClickLabel = onClickLabel,
        onLongClickLabel = onLongClickLabel,
        onLongClick = onLongClick,
        onDoubleClick = onDoubleClick,
        onClick = onClick,
        role = role,
        indication = indication,
        interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    )
}

/* Original author, tunjid*/

actual class PlatformDropTargetModifier(
    density: Float,
    window: ComposeWindow,
) : DropTargetModifier by dropTargetModifier() {
    init {
        val awtDropTarget = DropTarget()
        awtDropTarget.addDropTargetListener(
            dropTargetListener(
                dropTargetModifier = this,
                density = density
            )
        )
        window.contentPane.dropTarget = awtDropTarget
    }
}

private fun dropTargetListener(
    dropTargetModifier: DropTargetModifier,
    density: Float
) = object : DropTargetListener {
    override fun dragEnter(dtde: DropTargetDragEvent?) {
        if (dtde == null) return
        dropTargetModifier.onDragStarted(
            listOf(),
            Offset(
                dtde.location.x * density,
                dtde.location.y * density
            )
        )
        dropTargetModifier.onDragEntered()
    }

    override fun dragOver(dtde: DropTargetDragEvent?) {
        if (dtde == null) return
        dropTargetModifier.onDragMoved(
            Offset(
                dtde.location.x * density,
                dtde.location.y * density
            )
        )
    }

    override fun dropActionChanged(dtde: DropTargetDragEvent?) = Unit

    override fun dragExit(dte: DropTargetEvent?) {
        dropTargetModifier.onDragExited()
        dropTargetModifier.onDragEnded()
    }

    override fun drop(dtde: DropTargetDropEvent?) {
        if (dtde == null) return dropTargetModifier.onDragEnded()

        dtde.acceptDrop(DnDConstants.ACTION_REFERENCE)
        dtde.dropComplete(
            dropTargetModifier.onDropped(
                dtde.fileUris(),
                Offset(
                    dtde.location.x * density,
                    dtde.location.y * density
                )
            )
        )
        dropTargetModifier.onDragEnded()
    }
}

private fun DropTargetDropEvent.fileUris(): List<Uri> = transferable
    .getTransferData(DataFlavor.javaFileListFlavor)
    .let { it as? List<*> ?: listOf<File>() }
    .filterIsInstance<File>()
    .map(::FileUri)