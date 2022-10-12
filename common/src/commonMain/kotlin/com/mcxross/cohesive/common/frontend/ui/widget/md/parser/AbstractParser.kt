package com.mcxross.cohesive.common.frontend.ui.widget.md.parser

import com.mcxross.cohesive.common.frontend.ui.widget.MarkdownConfig
import com.mcxross.cohesive.common.frontend.ui.widget.md.markup.Element
import kotlinx.coroutines.flow.Flow

interface AbstractParser {

    fun setMarkdownConfig(config: MarkdownConfig): Parser

    fun setMarkdownContent(content: String): Parser

    fun build(): Flow<Element>

}