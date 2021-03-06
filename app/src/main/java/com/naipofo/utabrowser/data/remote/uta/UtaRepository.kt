package com.naipofo.utabrowser.data.remote.uta

import com.naipofo.utabrowser.data.Result
import com.naipofo.utabrowser.data.local.pageCache.PageCacheRepository
import com.naipofo.utabrowser.data.local.searchSuggestions.SearchSuggestionsRepository
import com.naipofo.utabrowser.data.model.LyricListing
import com.naipofo.utabrowser.data.model.LyricPage
import com.naipofo.utabrowser.data.model.LyricsSearchFilters
import com.naipofo.utabrowser.data.remote.uta.response.toModel
import com.naipofo.utabrowser.data.tryResult

class UtaRepository(
    private val api: UtaApi,
    private val extractor: UtaExtractor,
    private val pageCacheRepository: PageCacheRepository,
    private val searchSuggestionsRepository: SearchSuggestionsRepository
) {
    private var lastQuery: LyricsSearchFilters? = null
    private var lastData: List<LyricListing> = listOf()
    private var topSongs: List<LyricListing>? = null

    suspend fun getLyricPage(url: String): Result<LyricPage> = tryResult {
        pageCacheRepository.getPage(url)
            ?: extractor.getPageData(url).let { data ->
                LyricPage(
                    listing = (lastData.firstOrNull { it.url == url }
                        ?: topSongs!!.first { it.url == url }).let { listing ->
                        if (listing.image.contains("noImage") && data.youtubeVideos.isNotEmpty()) {
                            listing.copy(image = data.youtubeVideos[0].thumbnailUrl)
                        } else listing
                    },
                    text = data.lyrics,
                    youtubeVideos = data.youtubeVideos
                ).also {
                    pageCacheRepository.savePage(it)
                }
            }
    }

    suspend fun searchSong(query: LyricsSearchFilters): Result<List<LyricListing>> = tryResult {
        if (lastQuery == query) return@tryResult lastData
        lastQuery = query
        api.searchWithFilters(query).items.map { it.toModel() }.also {
            lastData = it
            searchSuggestionsRepository.addSuggestions(it)
        }
    }

    suspend fun getTopSongs(): Result<List<LyricListing>> = tryResult {
        topSongs ?: api.getLyricRanking().items.map { it.toModel() }.also {
            topSongs = it
            searchSuggestionsRepository.addSuggestions(it)
        }
    }
}