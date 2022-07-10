package com.naipofo.utabrowser.ui.screens.song

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.naipofo.utabrowser.data.Result
import com.naipofo.utabrowser.data.local.favorites.FavoritesRepository
import com.naipofo.utabrowser.data.model.LyricNode
import com.naipofo.utabrowser.data.model.LyricPage
import com.naipofo.utabrowser.data.remote.uta.UtaRepository
import kotlinx.coroutines.launch
import org.kodein.di.compose.localDI
import org.kodein.di.instance

@Composable
fun SongRoute(url: String) {
    val scope = rememberCoroutineScope()
    val utaRepository: UtaRepository by localDI().instance()
    val favoritesRepository: FavoritesRepository by localDI().instance()

    var lyricData: Result<LyricPage>? by remember {
        mutableStateOf(null)
    }
    var isFavorite by remember {
        mutableStateOf(false)
    }

    SideEffect {
        scope.launch {
            lyricData = utaRepository.getLyricPage(url)
            isFavorite = favoritesRepository.isFavorite(url)
        }
    }

    fun toggleFavorite() {
        isFavorite = !isFavorite
        scope.launch { favoritesRepository.setFavorite(url, isFavorite) }
    }

    LazyColumn(Modifier.fillMaxSize()) {
        when (val data = lyricData) {
            is Result.Error -> {
                item { Text(text = "Error!:\n\n${data.exception}\n${data.exception.stackTrace}") }
                throw data.exception
            }
            is Result.Success -> {
                item {
                    Row {
                        Column {
                            Text(
                                text = data.data.listing.title,
                                style = MaterialTheme.typography.displayLarge
                            )
                            Text(
                                text = data.data.listing.artist,
                                style = MaterialTheme.typography.displaySmall
                            )
                        }
                        IconButton(onClick = { toggleFavorite() }) {
                            Icon(
                                imageVector = if (isFavorite) {
                                    Icons.Filled.Star
                                } else {
                                    Icons.Outlined.Star
                                }, contentDescription = "Favorite lyric"
                            )
                        }
                    }
                }
                val lines: MutableList<List<LyricNode>> = mutableListOf()
                var l = mutableListOf<LyricNode>()
                data.data.text.forEach {
                    if (it is LyricNode.LineBreak) {
                        lines.add(l)
                        l = mutableListOf()
                    } else {
                        l.add(it)
                    }
                }
                items(lines) {
                    FlowRow(
                        crossAxisAlignment = FlowCrossAxisAlignment.End
                    ) {
                        it.forEach {
                            when (it) {
                                is LyricNode.Text -> Text(
                                    text = it.string,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontSize = 21.sp,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                is LyricNode.Ruby -> Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = it.rt,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = .8f)
                                    )
                                    Text(
                                        text = it.rb,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontSize = 21.sp,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                                else -> {}
                            }
                        }
                    }
                }
            }
            null -> item {
                CircularProgressIndicator()
                Text(text = "Loading...")
            }
        }
    }
}