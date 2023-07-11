package xyz.mcxross.cohesive.ui.impl.view.md.parser


import xyz.mcxross.cohesive.model.MarkdownConfig
import xyz.mcxross.cohesive.ui.impl.view.md.markup.Element
import kotlinx.coroutines.flow.Flow

expect fun parseMarkdownContent(string: String): Flow<Element>

open class Parser : AbstractParser {

  private var currentConfig: MarkdownConfig? = null
  private var currentContent: String = ""

  companion object {
    private const val EMPTY_COMPONENT = "Empty Component"
  }

  override fun setMarkdownConfig(config: MarkdownConfig): Parser {
    this.currentConfig = config
    return this
  }

  override fun setMarkdownContent(content: String): Parser {
    this.currentContent = content
    return this
  }

  override fun build(): Flow<Element> {
    return parseMarkdownContent(currentContent)
  }
}
