package com.mcxross.cohesive.common.ui.view.editor.platform

import com.mcxross.cohesive.common.ui.view.editor.util.TextLines
import kotlinx.coroutines.CoroutineScope

expect val HomeFolder: File

interface File {
    val name: String
    val isDirectory: Boolean
    val children: List<File>
    val hasChildren: Boolean

    fun readLines(scope: CoroutineScope): TextLines
}