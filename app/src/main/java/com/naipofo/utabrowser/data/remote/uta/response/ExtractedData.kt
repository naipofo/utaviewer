package com.naipofo.utabrowser.data.remote.uta.response

import com.naipofo.utabrowser.data.model.LyricNode
import com.naipofo.utabrowser.data.model.YoutubeVideo
import kotlinx.serialization.Serializable

@Serializable
data class ExtractedData(
    val lyrics: List<LyricNode>,
    val youtubeVideos: List<YoutubeVideo>
)