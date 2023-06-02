package xyz.mcxross.cohesive.common.frontend.impl.ui.widget.md.markup

import androidx.compose.runtime.Composable

@Composable
fun MarkdownShieldComponentComposable(content: String) {
  /* val context = LocalContext.current
  AndroidView(factory = {
      WebView(context).apply {
          this.loadUrl(content)
      }
  }, modifier = Modifier.wrapContentWidth().wrapContentHeight().padding(5.dp))*/
}

data class MarkdownShieldComponent(val url: String) : Element
