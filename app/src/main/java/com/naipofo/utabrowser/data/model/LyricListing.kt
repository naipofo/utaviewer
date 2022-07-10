package com.naipofo.utabrowser.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LyricListing(
    val title: String,
    val artist: String,
    val image: String,
    val url: String,
)