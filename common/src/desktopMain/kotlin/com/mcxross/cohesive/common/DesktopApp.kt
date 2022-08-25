package com.mcxross.cohesive.common

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mcxross.cohesive.common.ui.component.CButton

@Preview
@Composable
fun AppPreview() {
    App()
}

@Preview
@Composable
fun Button() {
    CButton("Ok", modifier = Modifier.height(35.dp)) {}
}

