package com.naipofo.utabrowser.ui.screens.song

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.naipofo.utabrowser.data.model.LyricNode

fun LazyListScope.rubyRender(lyrics: List<LyricNode>, sizeMultiplier: Float, showRuby: Boolean) {
    val lines: MutableList<List<LyricNode>> = mutableListOf()
    var l = mutableListOf<LyricNode>()
    lyrics.forEach {
        if (it is LyricNode.LineBreak) {
            lines.add(l)
            l = mutableListOf()
        } else {
            l.add(it)
        }
    }

    val normalSize = 21.sp * sizeMultiplier
    val rubySize = 14.sp * sizeMultiplier
    items(lines) {
        FlowRow(
            crossAxisAlignment = FlowCrossAxisAlignment.End
        ) {
            it.forEach {
                when (it) {
                    is LyricNode.Text -> Text(
                        text = it.string,
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = normalSize,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    is LyricNode.Ruby -> Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (showRuby) DisableSelection {
                            Text(
                                text = it.rt,
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = rubySize,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = .8f)
                            )
                        }
                        Text(
                            text = it.rb,
                            style = MaterialTheme.typography.bodyLarge,
                            fontSize = normalSize,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    else -> {}
                }
            }
            Column {
                if (showRuby) Text(
                    text = "",
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = rubySize,
                )
                Text(
                    text = "",
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = normalSize,
                )
            }
        }
    }
}