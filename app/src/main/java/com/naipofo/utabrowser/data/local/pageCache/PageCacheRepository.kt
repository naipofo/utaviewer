package com.naipofo.utabrowser.data.local.pageCache

import com.naipofo.utabrowser.Database
import com.naipofo.utabrowser.data.DeflateUtils
import com.naipofo.utabrowser.data.model.LyricPage
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PageCacheRepository(
    private val database: Database
) {
    fun savePage(page: LyricPage) =
        database.pageCacheQueries.insert(
            page.listing.url,
            DeflateUtils.deflate(Json.encodeToString(page))
        )

    fun getPage(url: String): LyricPage? =
        database.pageCacheQueries.fetchOne(url).executeAsOneOrNull()
            ?.let { Json.decodeFromString(DeflateUtils.inflate(it)) }
}