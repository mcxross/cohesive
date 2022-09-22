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
        var activeEditor: Editor? = null

        editors.forEach {
            if (it.isSame(file.name)) {
                activeEditor = it
                activeEditor!!.selection = selection
            }
        }
        if(activeEditor == null) {
            activeEditor = Editor(file)
            activeEditor!!.selection = selection
            activeEditor!!.close = {
                close(activeEditor!!)
            }
            editors.add(activeEditor!!)
        }

        activeEditor!!.activate()
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