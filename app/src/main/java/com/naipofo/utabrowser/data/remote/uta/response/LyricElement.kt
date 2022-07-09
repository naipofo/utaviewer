package com.naipofo.utabrowser.data.remote.uta.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LyricElement(
    @SerialName("artist")
    val artist: String,
    @SerialName("beginning")
    val beginning: String,
    @SerialName("id")
    val id: Int,
    @SerialName("imageModel")
    val imageModel: ImageModel,
    @SerialName("title")
    val title: String,
    @SerialName("url")
    val url: String
)