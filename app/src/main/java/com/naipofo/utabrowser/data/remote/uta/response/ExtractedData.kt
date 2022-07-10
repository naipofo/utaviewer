package com.naipofo.utabrowser.data.remote.uta.response

import com.naipofo.utabrowser.data.model.LyricNode
import kotlinx.serialization.Serializable

@Serializable
data class ExtractedData(
    val lyrics: List<LyricNode>
)