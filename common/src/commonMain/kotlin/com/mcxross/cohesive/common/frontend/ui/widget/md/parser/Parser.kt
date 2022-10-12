package com.mcxross.cohesive.common.frontend.ui.widget.md.parser

import com.mcxross.cohesive.common.frontend.ui.widget.MarkdownConfig
import com.mcxross.cohesive.common.frontend.ui.widget.md.markup.*
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