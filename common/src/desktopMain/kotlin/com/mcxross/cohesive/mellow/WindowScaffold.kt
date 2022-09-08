package com.mcxross.cohesive.mellow

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

object Toast {
    var model: ToastModel by mutableStateOf(ToastModel())
    fun message(title: String = "", message: String) {
        model.toast(title = title, message = message)
    }
}

@Composable
fun WindowScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    content: @Composable() (ColumnScope.() -> Unit) = {},
) {
    DisableSelection {
        MellowTheme.Theme {
            Surface(
                modifier = modifier.fillMaxSize(),
                contentColor = contentColorFor(MaterialTheme.colors.surface)
            ) {
                Column {
                    topBar()
                    content()
                }

                Toast(model = Toast.model)
            }
        }
    }
}