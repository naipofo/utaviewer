package com.naipofo.utabrowser.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LyricPage(
    val listing: LyricListing,
    val text: List<LyricNode>,
    val youtubeVideos: List<YoutubeVideo>
)