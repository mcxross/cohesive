package com.mcxross.cohesive.mellow

import androidx.compose.runtime.mutableStateListOf

class Editors {
    private val selection = SingleSelection()

    internal var editors = mutableStateListOf<Editor>()
        private set

    internal val active: Editor? get() = selection.selected as Editor?

    fun open(
        file: File,
    ) {
        val editor = Editor(file)
        editor.selection = selection
        editor.close = {
            close(editor)
        }
        editors.add(editor)
        editor.activate()
    }

    private fun close(
        editor: Editor,
    ) {
        val index = editors.indexOf(editor)
        editors.remove(editor)
        if (editor.isActive) {
            selection.selected = editors.getOrNull(index.coerceAtMost(editors.lastIndex))
        }
    }
}