package com.mcxross.cohesive.common.frontend.impl.ui.widget.md.parser

import com.mcxross.cohesive.common.frontend.impl.ui.widget.md.markup.Element
import kotlinx.coroutines.flow.Flow

actual fun parseMarkdownContent(string: String): Flow<Element> {
  return kotlinx.coroutines.flow.flow {
    //emit(null)
  }
}
