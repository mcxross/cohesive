package com.mcxross.cohesive.common.frontend.ui.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mcxross.cohesive.common.frontend.ui.widget.md.markup.BoldText
import com.mcxross.cohesive.common.frontend.ui.widget.md.markup.Checkbox
import com.mcxross.cohesive.common.frontend.ui.widget.md.markup.Code
import com.mcxross.cohesive.common.frontend.ui.widget.md.markup.Element
import com.mcxross.cohesive.common.frontend.ui.widget.md.markup.Image
import com.mcxross.cohesive.common.frontend.ui.widget.md.markup.ItalicText
import com.mcxross.cohesive.common.frontend.ui.widget.md.markup.Link
import com.mcxross.cohesive.common.frontend.ui.widget.md.markup.MKStyledText
import com.mcxross.cohesive.common.frontend.ui.widget.md.markup.MKText
import com.mcxross.cohesive.common.frontend.ui.widget.md.markup.MarkdownShieldComponent
import com.mcxross.cohesive.common.frontend.ui.widget.md.markup.MarkdownShieldComponentComposable
import com.mcxross.cohesive.common.frontend.ui.widget.md.markup.MarkdownStyledTextComponent
import com.mcxross.cohesive.common.frontend.ui.widget.md.markup.Space
import com.mcxross.cohesive.common.frontend.ui.widget.md.markup.Text
import com.mcxross.cohesive.mellow.VerticalScrollbar
import kotlinx.coroutines.launch
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.parser.MarkdownParser

@Composable
fun Markdown(
  modifier: Modifier,
  content: String,
  config: MarkdownConfig,
  onLinkClickListener: (String, Int) -> Unit,
) {
  val scope = rememberCoroutineScope()

  scope.launch {
    val parsedTree =
      MarkdownParser(CommonMarkFlavourDescriptor()).buildMarkdownTreeFromString(content)

    parsedTree.children.forEach {
      println(it.type.name)
      println(content.subSequence(it.startOffset, it.endOffset))
      if (it.children.isNotEmpty()) {
        getNestedChildren(it, content)
      }
    }
  }

  Box(modifier = modifier) {
    val isScrollEnabled = config.isScrollEnabled
    val scrollState = rememberLazyListState()
    if (isScrollEnabled) {
      LazyColumn(
        state = scrollState,
      ) {
        scope.launch {
          /*parser.collect {
              println(it)
              item {
                  RenderComponent(it, config, onLinkClickListener)
              }
          }*/
        }

      }
    }

    VerticalScrollbar(
      modifier = Modifier.align(Alignment.CenterEnd),
      scrollState = scrollState,
    )

  }
}

@Composable
private fun RenderComponent(
  item: Element,
  config: MarkdownConfig,
  onLinkClickListener: (String, Int) -> Unit,
) {
  Surface {
    SelectionContainer {
      when (item) {
        is Code -> Code(
          text = item.codeBlock,
          config.colors?.get(
            MarkdownConfig.CODE_BACKGROUND_COLOR,
          ) ?: Color.Gray,
          config.colors?.get(MarkdownConfig.CODE_BLOCK_TEXT_COLOR) ?: Color.White,
        )

        is ItalicText -> ItalicText(
          text = item.text,
          config.colors?.get(
            MarkdownConfig.TEXT_COLOR,
          ) ?: Color.Black,
        )

        is BoldText -> BoldText(
          text = item.text,
          config.colors?.get(
            MarkdownConfig.TEXT_COLOR,
          ) ?: Color.Black,
        )

        is Link -> Link(
          item.text, item.link,
          config.colors?.get(
            MarkdownConfig.LINKS_COLOR,
          ) ?: Color.Black,
          config.isLinksClickable, onLinkClickListener,
        )

        is Checkbox -> Checkbox(
          item.text,
          config.colors?.get(MarkdownConfig.CHECKBOX_COLOR) ?: Color.Black,
          item.isChecked,
        )

        is MarkdownShieldComponent -> MarkdownShieldComponentComposable(item.url)
        is MKText -> Text(item.text, config.colors?.get(MarkdownConfig.TEXT_COLOR) ?: Color.Black)
        is Space -> Space()
        is Image -> Image(item.image, config.isImagesClickable, onLinkClickListener)
        is MarkdownStyledTextComponent -> MKStyledText(
          item.text, item.layer,
          color = config.colors?.get(
            MarkdownConfig.HASH_TEXT_COLOR,
          ) ?: Color.Black,
        )
      }
    }
  }

}

private fun getNestedChildren(node: ASTNode, string: String) {
  node.children.forEach {
    println(it.type.name)
    println(string.subSequence(it.startOffset, it.endOffset))
    if (it.children.isNotEmpty()) {
      getNestedChildren(it, string)
    }
  }
}
