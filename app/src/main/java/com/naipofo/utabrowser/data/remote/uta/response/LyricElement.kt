package com.naipofo.utabrowser.data.remote.uta.response


import com.naipofo.utabrowser.data.model.LyricListing
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

fun LyricElement.toModel() =
    LyricListing(title = title, artist = artist, image = imageModel.imageUrl, url = url)