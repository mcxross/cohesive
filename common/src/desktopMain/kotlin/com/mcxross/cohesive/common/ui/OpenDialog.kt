package com.mcxross.cohesive.common.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.mcxross.cohesive.common.editor.platform.HomeFolder
import com.mcxross.cohesive.common.editor.ui.filetree.FileTree
import com.mcxross.cohesive.common.editor.ui.filetree.FileTreeView
import com.mcxross.cohesive.common.ui.component.CDialog
import com.mcxross.cohesive.common.utils.WindowStateHolder


private fun close() {
    WindowStateHolder.isOpenDialogOpen = !WindowStateHolder.isOpenDialogOpen
}

@Composable
fun OpenDialog() {

    CDialog(title = "Open File or Project", width = 450.dp, height = 450.dp, negativeText = "Cancel", onNegative = {
        close()
    }, positiveText = "Ok", onClose = {
        close()
    }) {

        FileTreeView(FileTree(HomeFolder))

    }

}