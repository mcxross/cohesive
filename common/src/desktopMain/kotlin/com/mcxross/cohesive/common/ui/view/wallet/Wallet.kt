package com.mcxross.cohesive.common.ui.view.wallet

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
import com.mcxross.cohesive.mellow.Button
import com.mcxross.cohesive.common.utils.WindowStateHolder

@Composable
actual fun Wallet() {

    Box(Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {
        Column(Modifier.align(Alignment.Center)) {

            Text(
                text = "Create or Add Account(s) to Wallet",
                color = LocalContentColor.current.copy(alpha = 0.60f),
                fontSize = 20.sp,
                modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally)
            )

            Button(
                onClick = { WindowStateHolder.isImportAccountOpen = true },
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = "Import Account(s) into Wallet",
            )

            Button(
                onClick = { WindowStateHolder.isCreateAccountOpen = true },
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = "Create Account(s) in Wallet",
            )

        }
    }


}