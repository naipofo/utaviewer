package com.naipofo.utabrowser.data.remote.uta

import com.naipofo.utabrowser.data.remote.uta.response.LyricElement
import com.naipofo.utabrowser.data.remote.uta.response.UtaResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class UtaApi(
    private val client: HttpClient,
    private val key: String,
    private val domain: String
) {
    suspend fun searchByTitle(title: String): UtaResponse<LyricElement> =
        client.get("$domain/appApi/lyric/search?api_key=$key&title=$title").body()

    suspend fun getLyricRanking(): UtaResponse<LyricElement> =
        client.get("$domain/appApi/lyric/ranking?api_key=$key").body()
}