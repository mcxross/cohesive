package com.mcxross.cohesive.common.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.mcxross.cohesive.common.ui.component.CDialog
import com.mcxross.cohesive.common.utils.WindowStateHolder

private fun close() {
    WindowStateHolder.isCreateAccountOpen = !WindowStateHolder.isCreateAccountOpen

}

@Composable
fun CreateAccountDialog() {

    CDialog(title = "Create Account", negativeText = "Cancel", positiveText = "Create", onClose = {
        close()
    }) {

        Text("Create Account")

    }

}