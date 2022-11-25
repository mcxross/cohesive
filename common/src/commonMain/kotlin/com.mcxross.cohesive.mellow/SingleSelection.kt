package com.mcxross.cohesive.mellow

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class SingleSelection {
  var selected: EditorModel? by mutableStateOf(null)
}
