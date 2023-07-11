package xyz.mcxross.cohesive.ui.impl.view.md.parser

import xyz.mcxross.cohesive.model.MarkdownConfig
import xyz.mcxross.cohesive.ui.impl.view.md.markup.Element
import kotlinx.coroutines.flow.Flow

interface AbstractParser {

  fun setMarkdownConfig(config: MarkdownConfig): Parser

  fun setMarkdownContent(content: String): Parser

  fun build(): Flow<Element>
}
