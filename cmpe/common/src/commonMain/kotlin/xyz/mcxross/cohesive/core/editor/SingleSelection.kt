package xyz.mcxross.cohesive.core.editor

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import xyz.mcxross.cohesive.core.editor.EditorModel

class SingleSelection {
  var selected: EditorModel? by mutableStateOf(null)
}
