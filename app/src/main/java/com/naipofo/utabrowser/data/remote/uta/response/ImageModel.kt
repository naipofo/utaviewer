package com.naipofo.utabrowser.data.remote.uta.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageModel(
    @SerialName("imageSize")
    val imageSize: String,
    @SerialName("imageUrl")
    val imageUrl: String
)