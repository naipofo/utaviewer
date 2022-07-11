package com.naipofo.utabrowser.data.model

import kotlinx.serialization.Serializable

@Serializable
data class YoutubeVideo(
    val id: String,
    val title: String
) {
    val thumbnailUrl: String
        get() = "https://i.ytimg.com/vi/${id}/mqdefault.jpg"
}
