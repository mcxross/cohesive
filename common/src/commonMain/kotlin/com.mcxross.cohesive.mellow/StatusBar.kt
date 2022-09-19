package com.mcxross.cohesive.mellow

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StatusBar(
    content: @Composable (RowScope.() -> Unit),
) = Box(
    modifier = Modifier
        .height(25.dp)
        .fillMaxWidth()
        .padding(4.dp)
) {
    Row(modifier = Modifier.fillMaxHeight().align(Alignment.CenterEnd)) {
        content()
    }
}
