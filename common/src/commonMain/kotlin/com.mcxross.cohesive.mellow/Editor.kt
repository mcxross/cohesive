package com.mcxross.cohesive.mellow

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EditorTabs(
    model: Editors,
) = Row(
    modifier = Modifier.horizontalScroll(state = rememberScrollState())
) {
    for (editor in model.editors) {
        RectTab(
            text = editor.fileName,
            active = editor.isActive,
            onActivate = { editor.activate() },
            onClose = { editor.close?.let { it() } }
        )
    }
}

@Composable
fun Editor(
    model: Editor,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 13.sp,
    maxLineSymbols: Int = 120,
) = key(model) {
    with(LocalDensity.current) {
        SelectionContainer {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background,
            ) {
                val lines by loadableScoped(model.lines)

                if (lines != null) {
                    Box {
                        Lines(lines = lines!!, fontSize = fontSize)
                        Box(
                            modifier = Modifier
                                .offset(x = fontSize.toDp() * 0.5f * maxLineSymbols)
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
private fun Lines(
    lines: Editor.Lines,
    fontSize: TextUnit,
) = with(LocalDensity.current) {
    val maxNum = remember(lines.lineNumberDigitCount) {
        (1..lines.lineNumberDigitCount).joinToString(separator = "") { "9" }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        val scrollState = rememberLazyListState()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = scrollState,
        ) {
            items(lines.size) { index ->
                Box(
                    modifier = Modifier.height(fontSize.toDp() * 1.6f),
                ) {
                    Line(Modifier.align(Alignment.CenterStart), maxNum, lines[index], fontSize = fontSize)
                }
            }
        }

        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd),
            scrollState = scrollState
        )
    }
}

@Composable
private fun Line(
    modifier: Modifier,
    maxNum: String,
    line: Editor.Line,
    fontSize: TextUnit,
) = Row(
    modifier = modifier
) {
    DisableSelection {
        Box {
            LineNumber(
                number = maxNum,
                modifier = Modifier.alpha(0f),
                fontSize = fontSize,
            )
            LineNumber(
                number = line.number.toString(),
                modifier = Modifier.align(Alignment.CenterEnd),
                fontSize = fontSize,
            )
        }
    }
    LineContent(
        content = line.content,
        modifier = Modifier
            .weight(1f)
            .withoutWidthConstraints()
            .padding(start = 28.dp, end = 12.dp),
        fontSize = fontSize
    )
}


@Composable
private fun LineNumber(
    number: String,
    modifier: Modifier,
    fontSize: TextUnit,
) = Text(
    text = number,
    fontSize = fontSize,
    fontFamily = Fonts.jetbrainsMono(),
    color = LocalContentColor.current.copy(alpha = 0.30f),
    modifier = modifier.padding(start = 12.dp)
)

@Composable
private fun LineContent(
    content: Editor.Content,
    modifier: Modifier,
    fontSize: TextUnit,
) = Text(
    text = if (content.isCode) {
        codeString(content.value.value)
    } else {
        buildAnnotatedString {
            withStyle(MellowTheme.code.simple) {
                append(content.value.value)
            }
        }
    },
    fontSize = fontSize,
    fontFamily = Fonts.jetbrainsMono(),
    modifier = modifier,
    softWrap = false
)

private fun codeString(
    str: String,
) = buildAnnotatedString {
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

private fun AnnotatedString.Builder.addStyle(
    style: SpanStyle,
    text: String,
    regexp: String,
) {
    addStyle(style, text, Regex.fromLiteral(regexp))
}

private fun AnnotatedString.Builder.addStyle(
    style: SpanStyle,
    text: String,
    regexp: Regex,
) {
    for (result in regexp.findAll(text)) {
        addStyle(style, result.range.first, result.range.last + 1)
    }
}


@Composable
fun EditorEmpty(
    text: String = "No file opened",
) = Box(
    modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)
) {
    Column(
        modifier = Modifier.align(Alignment.Center)
    ) {
        Icon(
            Icons.Default.Code,
            contentDescription = null,
            tint = LocalContentColor.current.copy(alpha = 0.60f),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Text(
            text = text,
            color = LocalContentColor.current.copy(alpha = 0.60f),
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp)
        )
    }
}