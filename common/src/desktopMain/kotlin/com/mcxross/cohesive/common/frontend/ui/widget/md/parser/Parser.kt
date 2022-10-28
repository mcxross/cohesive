package com.mcxross.cohesive.common.frontend.ui.widget.md.parser

import com.mcxross.cohesive.common.frontend.ui.widget.MarkdownException
import com.mcxross.cohesive.common.frontend.ui.widget.MarkdownKeysManager
import com.mcxross.cohesive.common.frontend.ui.widget.md.markup.BoldText
import com.mcxross.cohesive.common.frontend.ui.widget.md.markup.Checkbox
import com.mcxross.cohesive.common.frontend.ui.widget.md.markup.Code
import com.mcxross.cohesive.common.frontend.ui.widget.md.markup.Element
import com.mcxross.cohesive.common.frontend.ui.widget.md.markup.Image
import com.mcxross.cohesive.common.frontend.ui.widget.md.markup.ItalicText
import com.mcxross.cohesive.common.frontend.ui.widget.md.markup.Link
import com.mcxross.cohesive.common.frontend.ui.widget.md.markup.MKText
import com.mcxross.cohesive.common.frontend.ui.widget.md.markup.MarkdownShieldComponent
import com.mcxross.cohesive.common.frontend.ui.widget.md.markup.MarkdownStyledTextComponent
import com.mcxross.cohesive.common.frontend.ui.widget.md.markup.Note
import com.mcxross.cohesive.common.frontend.ui.widget.md.markup.Space
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.StringReader

actual fun parseMarkdownContent(string: String): Flow<Element> = flow {

  if (string.isEmpty()) {
    throw MarkdownException()
  }

  val contentBufferReader = BufferedReader(StringReader(string))
  var line: String = ""
  var isCodeBlock = false
  val codeBlock = Code("")
  while (withContext(Dispatchers.IO) {
      contentBufferReader.readLine()
    }.also { line = it ?: "" } != null) {
    var isComponentTriggered = false
    if (line.isEmpty()) {
      emit(Space())
      continue
    }

    if (!isCodeBlock) {
      line = line.trim()
    } else {
      codeBlock.codeBlock += line + "\n"
    }

    if (line.contains(MarkdownKeysManager.CODE_BLOCK) && isCodeBlock) {
      isCodeBlock = false
      emit(codeBlock)
      continue
    }

    if (line.startsWith(MarkdownKeysManager.TEXT_H6) || line.startsWith(MarkdownKeysManager.TEXT_HASH_6)) {
      isComponentTriggered = true
      emit(
        MarkdownStyledTextComponent(
          line.replace(MarkdownKeysManager.TEXT_H6, "").replace(
            MarkdownKeysManager.TEXT_HASH_6, "",
          ),
          MarkdownKeysManager.TEXT_HASH_6,
        ),
      )
    }

    if (line.startsWith(MarkdownKeysManager.TEXT_H5) || line.startsWith(MarkdownKeysManager.TEXT_HASH_5) && !isComponentTriggered) {
      isComponentTriggered = true
      emit(
        MarkdownStyledTextComponent(
          line.replace(MarkdownKeysManager.TEXT_H5, "").replace(
            MarkdownKeysManager.TEXT_HASH_5, "",
          ),
          MarkdownKeysManager.TEXT_HASH_5,
        ),
      )
    }

    if (line.startsWith(MarkdownKeysManager.TEXT_H4) || line.startsWith(MarkdownKeysManager.TEXT_HASH_4) && !isComponentTriggered) {
      isComponentTriggered = true
      emit(
        MarkdownStyledTextComponent(
          line.replace(MarkdownKeysManager.TEXT_H4, "").replace(
            MarkdownKeysManager.TEXT_HASH_4, "",
          ),
          MarkdownKeysManager.TEXT_HASH_4,
        ),
      )
    }

    if (line.startsWith(MarkdownKeysManager.TEXT_H3) || line.startsWith(MarkdownKeysManager.TEXT_HASH_3) && !isComponentTriggered) {
      isComponentTriggered = true
      emit(
        MarkdownStyledTextComponent(
          line.replace(MarkdownKeysManager.TEXT_H3, "").replace(
            MarkdownKeysManager.TEXT_HASH_3, "",
          ),
          MarkdownKeysManager.TEXT_HASH_3,
        ),
      )

    }

    if (line.startsWith(MarkdownKeysManager.TEXT_H2) || line.startsWith(MarkdownKeysManager.TEXT_HASH_2) && !isComponentTriggered) {
      isComponentTriggered = true
      emit(
        MarkdownStyledTextComponent(
          line.replace(MarkdownKeysManager.TEXT_H2, "").replace(
            MarkdownKeysManager.TEXT_HASH_2, "",
          ),
          MarkdownKeysManager.TEXT_HASH_2,
        ),
      )

    }

    if ((line.startsWith(MarkdownKeysManager.TEXT_H1) || line.startsWith(MarkdownKeysManager.TEXT_HASH)) && !line.contains(
        "##",
      ) && !isComponentTriggered
    ) {
      isComponentTriggered = true
      emit(
        MarkdownStyledTextComponent(
          line.replace(MarkdownKeysManager.TEXT_H1, "").replace(
            MarkdownKeysManager.TEXT_HASH, "",
          ),
          MarkdownKeysManager.TEXT_HASH,
        ),
      )

    }

    if (line.startsWith(MarkdownKeysManager.IMAGE_START) && line.contains(MarkdownKeysManager.IMAGE_END)) {
      isComponentTriggered = true
      val imageUrl = line.split(MarkdownKeysManager.IMAGE_END).get(1).replace(")", "")
      if (isImagePath(imageUrl)) {
        emit(Image(imageUrl))
      } else {
        emit(MarkdownShieldComponent(imageUrl))
      }
    }

    if (line.startsWith(MarkdownKeysManager.IMAGE_WITHOUT_TAG_KEY)) {
      isComponentTriggered = true
      val imageUrl = line.split(MarkdownKeysManager.IMAGE_END).get(1).replace(")", "")
      if (isImagePath(imageUrl)) {
        emit(Image(imageUrl))
      } else {
        emit(MarkdownShieldComponent(imageUrl))
      }
    }

    if (line.startsWith(MarkdownKeysManager.NOTE)) {
      isComponentTriggered = true
      emit(Note(line.replace(MarkdownKeysManager.NOTE, "")))
    }

    if (line.startsWith(MarkdownKeysManager.CHECK_BOX_EMPTY)) {
      isComponentTriggered = true
      emit(Checkbox(false, line.replace(MarkdownKeysManager.CHECK_BOX_EMPTY, "")))
    }

    if (line.startsWith(MarkdownKeysManager.CHECK_BOX_EMPTY_2)) {
      isComponentTriggered = true
      emit(Checkbox(false, line.replace(MarkdownKeysManager.CHECK_BOX_EMPTY_2, "")))
    }

    if (line.startsWith(MarkdownKeysManager.CHECK_BOX_FILL)) {
      isComponentTriggered = true
      emit(Checkbox(true, line.replace(MarkdownKeysManager.CHECK_BOX_FILL, "")))
    }

    if (line.startsWith(MarkdownKeysManager.CHECK_BOX_FILL_2)) {
      isComponentTriggered = true
      emit(Checkbox(true, line.replace(MarkdownKeysManager.CHECK_BOX_FILL_2, "")))
    }

    if (line.startsWith(MarkdownKeysManager.LINK_START) && line.contains(MarkdownKeysManager.LINK_CONTAINS)) {
      val fragments = line.split(MarkdownKeysManager.LINK_CONTAINS)
      val text = fragments.get(0).replace("[", "")
      val link = fragments.get(1).replace(")", "")
      emit(Link(text, link))
      isComponentTriggered = true
    }

    if (line.contains(MarkdownKeysManager.BOLD)) {
      isComponentTriggered = true
      emit(BoldText(line.replace(MarkdownKeysManager.BOLD, "")))
    }

    if (line.contains(MarkdownKeysManager.ITALIC) && !isComponentTriggered) {
      isComponentTriggered = true
      emit(ItalicText(line.replace(MarkdownKeysManager.BOLD, "")))
    }

    if (line.contains(MarkdownKeysManager.CODE_BLOCK) && !isCodeBlock) {
      isCodeBlock = true
      continue
    }

    if (!isComponentTriggered && !isCodeBlock) {
      emit(MKText(line))
    }
  }
}

private fun isImagePath(imageUrl: String): Boolean {
  return imageUrl.contains(".png") || imageUrl.contains(".jpg") || imageUrl.contains(".jpeg") || imageUrl.contains(
    ".webp",
  ) || imageUrl.contains(".bmp")
}
