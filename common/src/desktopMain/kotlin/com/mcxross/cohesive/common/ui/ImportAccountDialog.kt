package com.mcxross.cohesive.common.ui

import androidx.compose.runtime.Composable
import com.mcxross.cohesive.common.ui.component.CDialog
import com.mcxross.cohesive.common.utils.WindowStateHolder

private fun close() {
    if (WindowStateHolder.isImportAccountOpen) WindowStateHolder.isImportAccountOpen = false
    else WindowStateHolder.isCreateAccountOpen = false
}

@Composable
fun ImportAccountDialog() {
    CDialog(
        title = "Import Account", negativeText = "Cancel", positiveText = "Import", onNegative = {
            close()
        }
    ) {
        close()
    }
}