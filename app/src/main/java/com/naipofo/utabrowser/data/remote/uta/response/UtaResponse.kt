package com.naipofo.utabrowser.data.remote.uta.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UtaResponse<T>(
    @SerialName("items")
    val items: List<T>,
    @SerialName("status")
    val status: Int
)