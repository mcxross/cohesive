package xyz.mcxross.cohesive.core.editor

import androidx.compose.runtime.mutableStateListOf
import xyz.mcxross.cohesive.designsystem.mellow.File

class EditorManager {
  private val selection = SingleSelection()

  internal var editorModels = mutableStateListOf<EditorModel>()
    private set

  internal val active: EditorModel?
    get() = selection.selected

  fun isActiveNonNull() = active != null

  fun open(file: File) {

    //TODO Check if file exists

    val activeEditorModel = editorModels.firstOrNull { it.isSame(file.absolutePath) }

    if (activeEditorModel != null) {
      activeEditorModel.selection = selection
      activeEditorModel.activate()
    } else {
      val newEditorModel = createEditorModel(file)
      newEditorModel.selection = selection
      newEditorModel.close = { close(newEditorModel) }
      editorModels.add(newEditorModel)
      newEditorModel.activate()
    }
  }


  private fun close(
    editorModel: EditorModel,
  ) {
    val index = editorModels.indexOf(editorModel)
    editorModels.remove(editorModel)
    if (editorModel.isActive) {
      selection.selected = editorModels.getOrNull(index.coerceAtMost(editorModels.lastIndex))
    }
  }
}
