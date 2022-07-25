package com.mcxross.cohesive.common.ui

import androidx.compose.runtime.Composable
import com.mcxross.cohesive.common.ui.component.CDialog
import com.mcxross.cohesive.common.utils.WindowStateHolder

@Composable
fun CreateAccountDialog() {

    CDialog(title = "Create Account", negativeText = "Cancel", positiveText = "Create") {
        if (WindowStateHolder.isImportAccountOpen)
            WindowStateHolder.isImportAccountOpen = false
        else
            WindowStateHolder.isCreateAccountOpen = false
    }

}