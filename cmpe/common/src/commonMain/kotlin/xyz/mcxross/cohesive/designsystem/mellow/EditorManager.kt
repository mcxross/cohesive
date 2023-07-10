package xyz.mcxross.cohesive.designsystem.mellow

import androidx.compose.runtime.mutableStateListOf

class EditorManager {
  private val selection = SingleSelection()

  internal var editorModels = mutableStateListOf<EditorModel>()
    private set

  internal val active: EditorModel?
    get() = selection.selected

  fun isActiveNonNull() = active != null

  fun open(
    file: File,
  ) {
    var activeEditorModel: EditorModel? = null

    editorModels.forEach {
      if (it.isSame(file.absolutePath)) {
        activeEditorModel = it
        activeEditorModel!!.selection = selection
      }
    }
    if (activeEditorModel == null) {
      activeEditorModel = createEditorModel(file)
      activeEditorModel!!.selection = selection
      activeEditorModel!!.close = { close(activeEditorModel!!) }
      editorModels.add(activeEditorModel!!)
    }

    activeEditorModel!!.activate()
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
