package com.mcxross.cohesive.common.ui.view.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.mcxross.cohesive.mellow.MellowTheme
import com.mcxross.cohesive.mellow.Fonts
import com.mcxross.cohesive.mellow.VerticalScrollbar
import com.mcxross.cohesive.common.ui.view.editor.util.loadableScoped
import com.mcxross.cohesive.common.ui.view.editor.util.withoutWidthConstraints
import kotlin.text.Regex.Companion.fromLiteral

@Composable
fun EditorView(model: Editor, settings: Settings) = key(model) {
    with (LocalDensity.current) {
        SelectionContainer {
            Surface(
                Modifier.fillMaxSize(),
                color = MaterialTheme.colors.surface,
            ) {
                val lines by loadableScoped(model.lines)

                if (lines != null) {
                    Box {
                        Lines(lines!!, settings)
                        Box(
                            Modifier
                                .offset(
                                    x = settings.fontSize.toDp() * 0.5f * settings.maxLineSymbols
                                )
                                .width(1.dp)
                                .fillMaxHeight()
                                .background(MaterialTheme.colors.onSurface)
                        )
                    }
                } else {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(36.dp)
                            .padding(4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun Lines(lines: Editor.Lines, settings: Settings) = with(LocalDensity.current) {
    val maxNum = remember(lines.lineNumberDigitCount) {
        (1..lines.lineNumberDigitCount).joinToString(separator = "") { "9" }
    }

    Box(Modifier.fillMaxSize()) {
        val scrollState = rememberLazyListState()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = scrollState
        ) {
            items(lines.size) { index ->
                Box(Modifier.height(settings.fontSize.toDp() * 1.6f)) {
                    Line(Modifier.align(Alignment.CenterStart), maxNum, lines[index], settings)
                }
            }
        }

        VerticalScrollbar(
            Modifier.align(Alignment.CenterEnd),
            scrollState
        )
    }
}

// Поддержка русского языка
// دعم اللغة العربية
// 中文支持
@Composable
private fun Line(modifier: Modifier, maxNum: String, line: Editor.Line, settings: Settings) {
    Row(modifier = modifier) {
        DisableSelection {
            Box {
                LineNumber(maxNum, Modifier.alpha(0f), settings)
                LineNumber(line.number.toString(), Modifier.align(Alignment.CenterEnd), settings)
            }
        }
        LineContent(
            line.content,
            modifier = Modifier
                .weight(1f)
                .withoutWidthConstraints()
                .padding(start = 28.dp, end = 12.dp),
            settings = settings
        )
    }
}

@Composable
private fun LineNumber(number: String, modifier: Modifier, settings: Settings) = Text(
    text = number,
    fontSize = settings.fontSize,
    fontFamily = Fonts.jetbrainsMono(),
    color = LocalContentColor.current.copy(alpha = 0.30f),
    modifier = modifier.padding(start = 12.dp)
)

@Composable
private fun LineContent(content: Editor.Content, modifier: Modifier, settings: Settings) = Text(
    text = if (content.isCode) {
        codeString(content.value.value)
    } else {
        buildAnnotatedString {
            withStyle(MellowTheme.code.simple) {
                append(content.value.value)
            }
        }
    },
    fontSize = settings.fontSize,
    fontFamily = Fonts.jetbrainsMono(),
    modifier = modifier,
    softWrap = false
)

private fun codeString(str: String) = buildAnnotatedString {
    withStyle(MellowTheme.code.simple) {
        val strFormatted = str.replace("\t", "    ")
        append(strFormatted)
        addStyle(MellowTheme.code.punctuation, strFormatted, ":")
        addStyle(MellowTheme.code.punctuation, strFormatted, "=")
        addStyle(MellowTheme.code.punctuation, strFormatted, "\"")
        addStyle(MellowTheme.code.punctuation, strFormatted, "[")
        addStyle(MellowTheme.code.punctuation, strFormatted, "]")
        addStyle(MellowTheme.code.punctuation, strFormatted, "{")
        addStyle(MellowTheme.code.punctuation, strFormatted, "}")
        addStyle(MellowTheme.code.punctuation, strFormatted, "(")
        addStyle(MellowTheme.code.punctuation, strFormatted, ")")
        addStyle(MellowTheme.code.punctuation, strFormatted, ",")
        addStyle(MellowTheme.code.keyword, strFormatted, "fun ")
        addStyle(MellowTheme.code.keyword, strFormatted, "val ")
        addStyle(MellowTheme.code.keyword, strFormatted, "var ")
        addStyle(MellowTheme.code.keyword, strFormatted, "private ")
        addStyle(MellowTheme.code.keyword, strFormatted, "internal ")
        addStyle(MellowTheme.code.keyword, strFormatted, "for ")
        addStyle(MellowTheme.code.keyword, strFormatted, "expect ")
        addStyle(MellowTheme.code.keyword, strFormatted, "actual ")
        addStyle(MellowTheme.code.keyword, strFormatted, "import ")
        addStyle(MellowTheme.code.keyword, strFormatted, "package ")
        addStyle(MellowTheme.code.value, strFormatted, "true")
        addStyle(MellowTheme.code.value, strFormatted, "false")
        addStyle(MellowTheme.code.value, strFormatted, Regex("[0-9]*"))
        addStyle(MellowTheme.code.annotation, strFormatted, Regex("^@[a-zA-Z_]*"))
        addStyle(MellowTheme.code.comment, strFormatted, Regex("^\\s*//.*"))
    }
}

private fun AnnotatedString.Builder.addStyle(style: SpanStyle, text: String, regexp: String) {
    addStyle(style, text, fromLiteral(regexp))
}

private fun AnnotatedString.Builder.addStyle(style: SpanStyle, text: String, regexp: Regex) {
    for (result in regexp.findAll(text)) {
        addStyle(style, result.range.first, result.range.last + 1)
    }
}