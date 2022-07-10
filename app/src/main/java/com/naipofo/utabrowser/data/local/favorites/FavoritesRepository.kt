package com.naipofo.utabrowser.data.local.favorites

import android.util.Log
import com.naipofo.utabrowser.Database

class FavoritesRepository(
    private var database: Database
) {
    fun isFavorite(url: String) = database.favoritesQueries.isFavorite(url).executeAsOne()
    fun setFavorite(url: String, state: Boolean) {
        Log.i("ASD", "setting to $state")
        if (state) database.favoritesQueries.insert(url) else database.favoritesQueries.remove(url)
    }
}