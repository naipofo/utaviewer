package com.naipofo.utabrowser.data.model

import kotlinx.serialization.Serializable

@Serializable
sealed interface LyricNode {
    @Serializable
    object LineBreak : LyricNode

    @Serializable
    data class Ruby(val rb: String, val rt: String) : LyricNode

    @Serializable
    data class Text(val string: String) : LyricNode

    companion object {
        val sample = listOf(
            Text("これは"),
            Ruby("魚", "さかな"),
            Text("です、"),
            Ruby("Fish", "フィッシュ"),
            Text("！"),
            LineBreak,
            Text("これは"),
            Ruby("犬", "いぬ"),
            Text("です、"),
            Ruby("Dog", "ドグ"),
            Text("！")
        )
    }
}