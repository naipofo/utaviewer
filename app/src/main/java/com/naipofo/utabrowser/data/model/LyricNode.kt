package com.naipofo.utabrowser.data.model

import kotlinx.serialization.Serializable

@Serializable
sealed interface LyricNode{
    @Serializable
    object LineBreak: LyricNode
    @Serializable
    data class Ruby(val rb: String, val rt: String): LyricNode
    @Serializable
    data class Text(val string: String): LyricNode
}