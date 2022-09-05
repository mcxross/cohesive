@file: JvmName("MellowButt")

package com.mcxross.cohesive.common.ui.view.editor

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.mcxross.cohesive.mellow.FileTree
import com.mcxross.cohesive.mellow.HomeFolder

@Composable
fun Editor() {
    val codeViewer = remember {
        val editors = Editors()

        CodeViewer(
            editors = editors,
            fileTree = FileTree(HomeFolder),
            settings = Settings()
        )
    }

    CodeViewerView(codeViewer)
}