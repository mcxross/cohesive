package xyz.mcxross.cohesive.mellow

import androidx.compose.runtime.State
import xyz.mcxross.cohesive.common.utils.Log
import kotlinx.coroutines.CoroutineScope

class EditorModel(
    val file: File,
    val lines: (backgroundScope: CoroutineScope) -> TextLines,
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
  fun isSame(file: String): Boolean = this.file.absolutePath.equals(file, ignoreCase = true)

  class Line(val number: Int, val content: Content)

  var textLines: TextLines? = null
    private set

  interface Lines {
    val lineNumberDigitCount: Int get() = size.toString().length
    val size: Int
    operator fun get(index: Int): Line
  }

  class Content(val value: State<String>, val isCode: Boolean)
}

internal fun createEditorModel(file: File) = EditorModel(
  file = file,
) { backgroundScope ->
  val textLines = try {
    file.readLines(backgroundScope)
  } catch (e: Throwable) {
    Log.e { "Failed to read file: $file" }
    e.printStackTrace()
    EmptyTextLines
  }

  textLines

  /*fun content(
    index: Int,
  ): EditorModel.Content {
    val text = textLines.get(index)
    val state = mutableStateOf(text)
    return EditorModel.Content(state, isCode)
  }

  object : EditorModel.EditorMap {
    override val size get() = textLines.size

    override fun get(
      index: Int,
    ) = EditorModel.Line(
      number = index + 1,
      content = content(index),
    )
  }*/
}
