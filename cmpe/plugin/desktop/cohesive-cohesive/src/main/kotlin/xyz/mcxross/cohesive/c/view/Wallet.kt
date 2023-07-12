package xyz.mcxross.cohesive.c.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import xyz.mcxross.cohesive.state.WindowState
import xyz.mcxross.cohesive.designsystem.mellow.Button
import xyz.mcxross.cohesive.designsystem.mellow.Dialog
import xyz.mcxross.cohesive.designsystem.mellow.Toast

@Composable
fun Wallet() {
  Box(Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {
    Column(Modifier.align(Alignment.Center)) {
      Text(
        text = "Create or Add Account(s) to Wallet",
        color = LocalContentColor.current.copy(alpha = 0.60f),
        fontSize = 20.sp,
        modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally),
      )

      Button(
        onClick = { WindowState.isImportAccountOpen = true },
        modifier = Modifier.align(Alignment.CenterHorizontally),
        text = "Import Account(s) into Wallet",
      )

      Button(
        onClick = { WindowState.isCreateAccountOpen = true },
        modifier = Modifier.align(Alignment.CenterHorizontally),
        text = "Create Account(s) in Wallet",
      )
    }
  }

  if (WindowState.isImportAccountOpen) {
    Dialog(
      onClose = { WindowState.isImportAccountOpen = !WindowState.isImportAccountOpen },
      text = "Import Account",
      negativeText = "Cancel",
      onNegative = { WindowState.isImportAccountOpen = !WindowState.isImportAccountOpen },
      neutralText = "",
      onNeutral = {},
      positiveText = "Import",
      onPositive = {},
      negativeEnable = true,
      neutralEnable = false,
      positiveEnable = true,
      width = 400.dp,
      height = 300.dp,
    ) {
      Text("Import Account")
    }
  }

  if (WindowState.isCreateAccountOpen) {
    Dialog(
      onClose = { WindowState.isCreateAccountOpen = !WindowState.isCreateAccountOpen },
      text = "Create Account",
      negativeText = "Cancel",
      onNegative = { WindowState.isCreateAccountOpen = !WindowState.isCreateAccountOpen },
      neutralText = "",
      onNeutral = {},
      positiveText = "Ok",
      onPositive = { Toast.message(title = "Create Account", message = "Failed...") },
      negativeEnable = true,
      neutralEnable = false,
      positiveEnable = true,
      width = 400.dp,
      height = 300.dp,
    ) {
      Text("Create Account")
    }
  }
}
