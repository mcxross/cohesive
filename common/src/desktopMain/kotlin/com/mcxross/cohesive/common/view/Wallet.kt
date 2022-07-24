package com.mcxross.cohesive.common.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberDialogState
import com.mcxross.cohesive.common.ui.CButton
import com.mcxross.cohesive.common.ui.TitleBar
import com.mcxross.cohesive.common.ui.WindowStateHolder

@Composable
fun WalletView() = Box(Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {
    Column(Modifier.align(Alignment.Center)) {

        Text(
            "Create or Add Account(s) to Wallet",
            color = LocalContentColor.current.copy(alpha = 0.60f),
            fontSize = 20.sp,
            modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally)
        )

        CButton(text = "Import Account(s) into Wallet", Modifier.align(Alignment.CenterHorizontally)) {
            WindowStateHolder.isImportAccountOpen = true
        }

        CButton(text = "Create Account(s) in Wallet", Modifier.align(Alignment.CenterHorizontally)) {

        }

    }
}

@Composable
fun ImportAccountDialog() {
    var isDialogOpen by remember { mutableStateOf(false) }
    Dialog(
        onCloseRequest = { isDialogOpen = false },
        undecorated = true,
        resizable = false,
        state = rememberDialogState(position = WindowPosition(Alignment.Center))
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            contentColor = contentColorFor(MaterialTheme.colors.surface)
        ) {
            Column {
                TitleBar()
            }
        }
    }
}