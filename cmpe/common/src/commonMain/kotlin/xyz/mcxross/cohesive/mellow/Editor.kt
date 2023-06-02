package xyz.mcxross.cohesive.mellow

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun EditorTabs(
    editorCompositeContainer: EditorCompositeContainer,
    panelState: PanelState,
) =
  Row(
    modifier = Modifier.horizontalScroll(state = rememberScrollState()),
  ) {
    val editorManager = editorCompositeContainer.editorManager
    val coroutineScope = rememberCoroutineScope()
    for (editor in editorManager.editorModels) {
      RectTab(
        text = editor.file.name,
        active = editor.isActive,
        onActivate = { editor.activate() },
        onDoubleClick = {
          editor.activate()
          coroutineScope.launch {
            delay(400)
            panelState.toggle()
          }
        },
        onClose = { editor.close?.let { it() } },
      )
    }
  }

@Composable
fun EditorTabs(
    editorManager: EditorManager,
) =
  Row(
    modifier = Modifier.horizontalScroll(state = rememberScrollState()),
  ) {
    for (editor in editorManager.editorModels) {
      RectTab(
        text = editor.file.name,
        active = editor.isActive,
        onActivate = { editor.activate() },
        onClose = { editor.close?.let { it() } },
      )
    }
  }

@Composable
fun Editor(
    model: EditorModel,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 13.sp,
    maxLineSymbols: Int = 120,
) =
  key(model) {
    with(LocalDensity.current) {
      SelectionContainer {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colors.background,
        ) {
          val textLines by loadableScoped(model.lines)
          if (textLines != null) {
            Box {
              EditorMap(textLines = textLines!!, fontSize = fontSize)
              Box(
                modifier =
                  Modifier.offset(x = fontSize.toDp() * 0.5f * maxLineSymbols)
                    .width(1.dp)
                    .fillMaxHeight()
                    .background(MaterialTheme.colors.onSurface),
              )
            }
          } else {
            Box { Progress(modifier = Modifier.align(Alignment.Center)) }
          }
        }
      }
    }
  }

@Composable
internal expect fun EditorMap(
    textLines: TextLines,
    fontSize: TextUnit,
)

@Composable
internal fun LineNumber(
  modifier: Modifier,
  maxNum: String,
  line: Int,
  fontSize: TextUnit,
) = DisableSelection {
  Box(
    modifier = modifier,
  ) {
    Number(
      number = maxNum,
      modifier = Modifier.alpha(0f),
      fontSize = fontSize,
    )
    Number(
      number = line.toString(),
      modifier = Modifier.align(Alignment.Center),
      fontSize = fontSize,
    )
  }
}

@Composable
private fun Number(
  number: String,
  modifier: Modifier,
  fontSize: TextUnit,
) =
  Text(
    text = number,
    fontSize = fontSize,
    fontFamily = Fonts.jetbrainsMono(),
    color = LocalContentColor.current.copy(alpha = 0.30f),
    modifier = modifier.padding(start = 12.dp),
  )

@Composable
expect fun TextField(
  text: String,
  isCode: Boolean,
  modifier: Modifier,
  fontSize: TextUnit,
  onScroll: (Float) -> Unit,
)

// content: EditorModel.TextField
fun highlight(text: String, isCode: Boolean): AnnotatedString {
  return if (isCode) {
    codeString(text)
  } else {
    buildAnnotatedString { withStyle(MellowTheme.code.simple) { append(text) } }
  }
}

fun codeString(
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
) =
  Box(
    modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background),
  ) {
    Column(
      modifier = Modifier.align(Alignment.Center),
    ) {
      Icon(
        Icons.Default.Code,
        contentDescription = null,
        tint = LocalContentColor.current.copy(alpha = 0.60f),
        modifier = Modifier.align(Alignment.CenterHorizontally),
      )

      Text(
        text = text,
        color = LocalContentColor.current.copy(alpha = 0.60f),
        fontSize = 20.sp,
        modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp),
      )
    }
  }
