package com.naipofo.utabrowser.data.remote.uta

import com.naipofo.utabrowser.Database
import com.naipofo.utabrowser.data.Result
import com.naipofo.utabrowser.data.model.LyricListing
import com.naipofo.utabrowser.data.model.LyricPage
import com.naipofo.utabrowser.data.remote.uta.response.toModel
import com.naipofo.utabrowser.data.tryResult
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class UtaRepository(
    private val api: UtaApi,
    private val extractor: UtaExtractor,
    private val database: Database
) {
    private var lastString = ""
    private var lastData: List<LyricListing> = listOf()
    private var topSongs: List<LyricListing>? = null

    suspend fun getLyricPage(url: String): Result<LyricPage> = tryResult {
        val cashed = database.pageCacheQueries.fetchOne(url).executeAsOneOrNull()
        if (cashed != null) {
            Json.decodeFromString(cashed)
        } else {
            val data = extractor.getPageData(url)
            LyricPage(
                listing = lastData.firstOrNull { it.url == url }
                    ?: topSongs!!.first { it.url == url },
                text = data.lyrics,
                youtubeVideos = data.youtubeVideos
            ).also {
                database.pageCacheQueries.insert(url, Json.encodeToString(it))
            }
        }
    }

    suspend fun searchSong(title: String): Result<List<LyricListing>> = tryResult {
        if (lastString == title) return@tryResult lastData
        lastString = title
        api.searchByTitle(title).items.map { it.toModel() }.also {
            lastData = it
        }
    }

    suspend fun getTopSongs(): Result<List<LyricListing>> = tryResult {
        if (topSongs != null) return@tryResult topSongs!!
        api.getLyricRanking().items.map { it.toModel() }.also {
            topSongs = it
        }
    }
}