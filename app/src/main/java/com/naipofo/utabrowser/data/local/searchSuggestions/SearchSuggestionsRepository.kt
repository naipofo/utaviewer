package com.naipofo.utabrowser.data.local.searchSuggestions

import com.naipofo.utabrowser.Database
import com.naipofo.utabrowser.data.model.LyricListing

class SearchSuggestionsRepository(
    private val database: Database
) {
    fun getSuggestions(query: String): List<SearchSuggestion> =
        database.searchSuggestionsQueries.getSuggestions(query).executeAsList().map {
            SearchSuggestion(it.text, SearchSuggestionType.valueOf(it.type))
        }

    private fun addSuggestion(suggestion: String, type: SearchSuggestionType) =
        database.searchSuggestionsQueries.insert(suggestion, type.name)

    fun addSuggestions(listings: List<LyricListing>) = listings.forEach {
        addSuggestion(it.title, SearchSuggestionType.SONG)
        addSuggestion(it.artist, SearchSuggestionType.ARTIST)
    }
}