package xyz.mcxross.cohesive.common.frontend.impl.ui.widget.md.parser


import xyz.mcxross.cohesive.common.frontend.impl.ui.widget.MarkdownConfig
import xyz.mcxross.cohesive.common.frontend.impl.ui.widget.md.markup.Element
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
    return xyz.mcxross.cohesive.common.frontend.impl.ui.widget.md.parser.parseMarkdownContent(currentContent)
  }
}
