package com.naipofo.utabrowser.data.local.favorites

import com.naipofo.utabrowser.Database
import com.naipofo.utabrowser.data.Result
import com.naipofo.utabrowser.data.model.LyricListing
import com.naipofo.utabrowser.data.remote.uta.UtaRepository

class FavoritesRepository(
    private var database: Database,
    private var utaRepository: UtaRepository
) {
    fun isFavorite(url: String) = database.favoritesQueries.isFavorite(url).executeAsOne()
    fun setFavorite(url: String, state: Boolean) =
        if (state) database.favoritesQueries.insert(url) else database.favoritesQueries.remove(url)

    suspend fun getFavorites(): List<LyricListing> =
        database.favoritesQueries.selectAll().executeAsList().mapNotNull {
            when (val e = utaRepository.getLyricPage(it)) {
                is Result.Error -> null
                is Result.Success -> e.data
            }?.listing
        }
}