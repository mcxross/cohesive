package com.mcxross.cohesive.common.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.mcxross.cohesive.common.ui.component.CDialog
import com.mcxross.cohesive.common.utils.WindowStateHolder

private fun close() {
    WindowStateHolder.isImportAccountOpen = !WindowStateHolder.isImportAccountOpen
}

@Composable
fun ImportAccountDialog() {
    CDialog(
        title = "Import Account", negativeText = "Cancel", positiveText = "Import", onNegative = {
            close()
        }, onClose = {
            close()
        }
    ) {
        Text("Import Account")
    }
}