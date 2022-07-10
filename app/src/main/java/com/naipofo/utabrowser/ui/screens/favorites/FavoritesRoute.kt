package com.naipofo.utabrowser.ui.screens.favorites

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.naipofo.utabrowser.data.local.favorites.FavoritesRepository
import com.naipofo.utabrowser.data.model.LyricListing
import com.naipofo.utabrowser.ui.LyricTile
import com.naipofo.utabrowser.ui.LyricTileSeparator
import kotlinx.coroutines.launch
import org.kodein.di.compose.localDI
import org.kodein.di.instance

@Composable
fun FavoritesRoute(showLyric: (url: String) -> Unit) {
    val scope = rememberCoroutineScope()
    val favoritesRepository: FavoritesRepository by localDI().instance()
    var favoriteLyrics: List<LyricListing>? by remember { mutableStateOf(null) }

    fun loadFavorites() = scope.launch {
        favoriteLyrics = favoritesRepository.getFavorites()
    }

    SideEffect { loadFavorites() }

    LazyColumn {
        item {
            Text(text = "Favourite", style = MaterialTheme.typography.displaySmall)
        }
        when (val data = favoriteLyrics) {
            null -> item {
                Column {
                    CircularProgressIndicator()
                    Text(text = "Loading...")
                }
            }
            else -> when (data.size) {
                0 -> item {
                    Text(text = "No favorites")
                }
                else -> itemsIndexed(data) { index, item ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        LyricTile(
                            data = item,
                            onClick = { showLyric(item.url) },
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = {
                                favoritesRepository.setFavorite(item.url, false)
                                loadFavorites()
                            },
                            Modifier
                                .padding(end = 16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Remove Song"
                            )
                        }
                    }
                    if (index < data.lastIndex) LyricTileSeparator()
                }
            }
        }
    }
}