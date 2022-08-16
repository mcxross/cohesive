package com.mcxross.cohesive.common.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberDialogState
import com.mcxross.cohesive.common.ui.component.CButton

@Composable
private fun Body(modifier: Modifier, content: @Composable () -> Unit) {
    Box(modifier = modifier) {
        content()
    }
}

@Composable
private fun Footer(
    modifier: Modifier,
    negativeText: String,
    neutralText: String,
    positiveText: String,
    onNegative: () -> Unit,
    onNeutral: () -> Unit,
    onPositive: () -> Unit,
    negativeEnable: Boolean,
    neutralEnable: Boolean,
    positiveEnable: Boolean,
) {
    Column(modifier = modifier) {
        Divider()
        Box(modifier = Modifier.fillMaxWidth().height(55.dp).padding(end = 5.dp)) {
            Row(modifier = Modifier.fillMaxHeight().align(Alignment.CenterEnd)) {
                if (positiveText.isNotEmpty()) {
                    CButton(
                        text = positiveText,
                        enabled = positiveEnable,
                        modifier = Modifier.height(35.dp).align(Alignment.CenterVertically)
                    ) {
                        onPositive()
                    }
                }

                if (neutralText.isNotEmpty()) {
                    CButton(
                        text = neutralText,
                        enabled = neutralEnable,
                        modifier = Modifier.height(35.dp).align(Alignment.CenterVertically)
                    ) {
                        onNeutral()
                    }
                }

                if (negativeText.isNotEmpty()) {
                    CButton(
                        text = negativeText,
                        enabled = negativeEnable,
                        modifier = Modifier.height(35.dp).align(Alignment.CenterVertically)
                    ) {
                        onNegative()
                    }
                }

            }
        }
    }
}

@Composable
fun CDialog(
    title: String,
    width: Dp = 400.dp,
    height: Dp = 300.dp,
    negativeText: String = "",
    neutralText: String = "",
    positiveText: String = "",
    onNegative: () -> Unit = {},
    onNeutral: () -> Unit = {},
    onPositive: () -> Unit = {},
    negativeEnable: Boolean = true,
    neutralEnable: Boolean = true,
    positiveEnable: Boolean = true,
    onClose: () -> Unit,
    content: @Composable () -> Unit
) {

    var isDialogOpen by remember { mutableStateOf(false) }
    Dialog(
        onCloseRequest = { isDialogOpen = false },
        undecorated = true,
        resizable = false,
        state = rememberDialogState(position = WindowPosition(Alignment.Center), width = width, height = height),
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            contentColor = contentColorFor(MaterialTheme.colors.surface)
        ) {

            Box(modifier = Modifier.fillMaxSize()) {
                Body(Modifier.matchParentSize().align(Alignment.Center).padding(top = 30.dp, bottom = 57.dp), content)
                TitleBar(title, modifier = Modifier.align(Alignment.TopStart), onClose)
                Footer(modifier = Modifier.fillMaxWidth().align(Alignment.BottomStart), negativeText, neutralText, positiveText, onNegative, onNeutral, onPositive, negativeEnable, neutralEnable, positiveEnable)
            }

        }
    }

}