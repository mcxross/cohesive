package com.mcxross.cohesive.common.ui.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.mcxross.cohesive.common.ui.view.View
import com.mcxross.cohesive.common.ui.view.editor.platform.HomeFolder
import com.mcxross.cohesive.common.ui.view.editor.filetree.FileTree
import com.mcxross.cohesive.common.ui.view.editor.filetree.FileTreeView
import com.mcxross.cohesive.common.ui.component.CDialog
import com.mcxross.cohesive.common.utils.WindowStateHolder

private fun close() {
    WindowStateHolder.isOpenDialogOpen = !WindowStateHolder.isOpenDialogOpen
}

@Composable
fun OpenDialog() {

    CDialog(title = "Open File or Project", width = 450.dp, height = 450.dp, negativeText = "Cancel", onNegative = {
        close()
    }, positiveText = "Ok", onPositive = {
        WindowStateHolder.view = View.EDITOR
        close()
    }, onClose = {
        close()
    }) {

        FileTreeView(FileTree(HomeFolder))

    }

}