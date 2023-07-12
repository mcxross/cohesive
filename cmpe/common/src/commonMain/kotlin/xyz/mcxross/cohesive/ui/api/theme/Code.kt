package xyz.mcxross.cohesive.ui.api.theme

import androidx.compose.ui.text.SpanStyle

interface Code {
  val simple: SpanStyle
  val value: SpanStyle
  val keyword: SpanStyle
  val punctuation: SpanStyle
  val annotation: SpanStyle
  val comment: SpanStyle
}
