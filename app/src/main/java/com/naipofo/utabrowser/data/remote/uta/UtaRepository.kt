package com.naipofo.utabrowser.data.remote.uta

import com.naipofo.utabrowser.data.Result
import com.naipofo.utabrowser.data.remote.uta.response.LyricElement
import com.naipofo.utabrowser.data.tryResult

class UtaRepository(
    private val api: UtaApi,
    private val extractor: UtaExtractor
) {
    suspend fun getSongData(url: String): Result<ExtractedData> = tryResult {
        extractor.getPageData(url)
    }

    suspend fun searchSong(title:String): Result<List<LyricElement>> = tryResult {
        api.searchByTitle(title).items
    }

    suspend fun getTopSongs(): Result<List<LyricElement>> = tryResult {
        api.getLyricRanking().items
    }
}