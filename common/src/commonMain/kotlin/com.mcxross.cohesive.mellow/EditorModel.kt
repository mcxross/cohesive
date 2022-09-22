package com.mcxross.cohesive.mellow

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope

class Editor(
    val fileName: String,
    val lines: (backgroundScope: CoroutineScope) -> Lines,
) {
    var close: (() -> Unit)? = null
    lateinit var selection: SingleSelection

    val isActive: Boolean
        get() = selection.selected === this

    fun activate() {
        selection.selected = this
    }

    //Note: This check isn't safe, but it's good enough for now
    //May break on different OSes
    fun isSame(file: String): Boolean {
        return this.fileName.equals(file, ignoreCase = true)
    }

    class Line(val number: Int, val content: Content)

    interface Lines {
        val lineNumberDigitCount: Int get() = size.toString().length
        val size: Int
        operator fun get(index: Int): Line
    }

    class Content(val value: State<String>, val isCode: Boolean)
}

internal fun Editor(file: File) = Editor(
    fileName = file.name
) { backgroundScope ->
    val textLines = try {
        file.readLines(backgroundScope)
    } catch (e: Throwable) {
        e.printStackTrace()
        EmptyTextLines
    }
    val isCode = file.name.endsWith(".kt", ignoreCase = true)

    fun content(
        index: Int,
    ): Editor.Content {
        val text = textLines.get(index)
        val state = mutableStateOf(text)
        return Editor.Content(state, isCode)
    }

    object : Editor.Lines {
        override val size get() = textLines.size

        override fun get(
            index: Int,
        ) = Editor.Line(
            number = index + 1,
            content = content(index)
        )
    }
}
