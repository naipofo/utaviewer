package com.naipofo.utabrowser.data.remote.uta

data class ExtractedData(
    val title: String,
    val artist: String,
    val lyrics: List<LyricNode>,
)

sealed interface LyricNode{
    object LineBreak: LyricNode
    data class Ruby(val rb: String, val rt: String): LyricNode
    data class Text(val string: String): LyricNode
}