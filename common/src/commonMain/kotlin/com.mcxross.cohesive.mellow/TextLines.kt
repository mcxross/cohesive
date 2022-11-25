package com.mcxross.cohesive.mellow

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

interface TextLines {
  val size: Int

  var isCode: Boolean
  val text: State<String>
    get() = mutableStateOf("")

  fun get(index: Int): String
}

object EmptyTextLines : TextLines {
  override val size: Int
    get() = 0

  override var isCode: Boolean = false

  override fun get(index: Int): String = ""
}
