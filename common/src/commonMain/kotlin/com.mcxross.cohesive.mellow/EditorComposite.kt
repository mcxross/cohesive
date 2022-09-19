package com.mcxross.cohesive.mellow

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

private val MinFontSize = 6.sp
private val MaxFontSize = 40.sp

private operator fun TextUnit.minus(other: TextUnit) = (value - other.value).sp
private operator fun TextUnit.div(other: TextUnit) = value / other.value

private fun Density.scale(scale: Float) = Density(density * scale, fontScale * scale)


@Composable
fun EditorComposite(
    text: String = "Files",
    dirPath: String = "",
) {

    val model = remember {
        val editors = Editors()

        CodeViewer(
            editors = editors,
            fileTree = FileTree(HomeFolder) { editors.open(it) },
        )
    }

    val panelState = remember { PanelState() }

    val animatedSize = if (panelState.splitter.isResizing) {
        if (panelState.isExpanded) panelState.expandedSize else panelState.collapsedSize
    } else {
        animateDpAsState(
            if (panelState.isExpanded) panelState.expandedSize else panelState.collapsedSize,
            SpringSpec(stiffness = Spring.StiffnessLow)
        ).value
    }

    VerticalSplittable(
        modifier = Modifier.fillMaxSize(),
        splitterState = panelState.splitter,
        onResize = {
            panelState.expandedSize = (panelState.expandedSize + it).coerceAtLeast(panelState.expandedSizeMin)
        }
    ) {
        ResizablePanel(
            modifier = Modifier.width(animatedSize).fillMaxHeight(),
            state = panelState
        ) {
            Column {
                FileTreeTab(text = text)
                FileTree(model = model.fileTree)
            }
        }

        Box {
            if (model.editors.active != null) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    EditorTabs(model = model.editors)
                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        Crossfade(model.editors.active) {
                            it?.let { it1 -> Editor(model = it1) }
                        }
                    }
                    /*StatusBar {
                        Text(
                            text = "Text size",
                            modifier = Modifier.align(Alignment.CenterVertically),
                            color = LocalContentColor.current.copy(alpha = 0.60f),
                            fontSize = 12.sp
                        )

                        Spacer(Modifier.width(8.dp))

                        CompositionLocalProvider(LocalDensity provides LocalDensity.current.scale(0.5f)) {
                            Slider(
                                value = (model.settings.fontSize - com.mcxross.cohesive.common.frontend.ui.view.editor.MinFontSize) / (com.mcxross.cohesive.common.frontend.ui.view.editor.MaxFontSize - com.mcxross.cohesive.common.frontend.ui.view.editor.MinFontSize),
                                onValueChange = {
                                    model.settings.fontSize =
                                        androidx.compose.ui.unit.lerp(
                                            MinFontSize,
                                            MaxFontSize,
                                            it
                                        )
                                },
                                modifier = Modifier.width(240.dp).align(Alignment.CenterVertically)
                            )
                        }

                    }*/
                }
            } else {
                EditorEmpty(
                    text = "To view file open it from the file tree",
                )
            }
        }
    }
}