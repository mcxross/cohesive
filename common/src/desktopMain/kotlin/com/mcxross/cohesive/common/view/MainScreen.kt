package com.mcxross.cohesive.common.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.window.WindowScope
import com.mcxross.cohesive.common.ui.TitleMenuBar
import com.mcxross.cohesive.common.ui.WindowStateHolder
import com.mcxross.cohesive.common.ui.common.AppTheme

@Composable
fun WindowScope.MainScreen() {
    DisableSelection {
        MaterialTheme(colors = AppTheme.getColors()) {

            Surface(
                modifier = Modifier.fillMaxSize(),
                contentColor = contentColorFor(MaterialTheme.colors.surface)
            ) {
                Column {
                    TitleMenuBar()

                    Divider()

                    with(LocalDensity.current) {
                        if (WindowStateHolder.view == View.EXPLORER) {
                            ExplorerView()
                        } else {
                            WalletView()
                            if(WindowStateHolder.isImportAccountOpen) {
                                ImportAccountDialog()
                            }

                        }
                    }

                }

            }


        }

    }


}
