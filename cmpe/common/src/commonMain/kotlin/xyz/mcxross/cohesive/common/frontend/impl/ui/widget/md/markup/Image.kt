package xyz.mcxross.cohesive.common.frontend.impl.ui.widget.md.markup

import androidx.compose.runtime.Composable

@Composable
fun Image(
  imageUrl: String,
  isEnabled: Boolean,
  onLinkClickListener: (String, Int) -> Unit,
) {
  /*AsyncImage(
  model = imageUrl,
  contentDescription = "Image Url Here",
  modifier = Modifier.clickable(enabled = isEnabled) {
      onLinkClickListener(
          imageUrl,
          MarkdownConfig.IMAGE_TYPE
      )
  })*/
}

data class Image(
  val image: String,
) : Element
