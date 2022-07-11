package com.naipofo.utabrowser.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.naipofo.utabrowser.data.Result
import com.naipofo.utabrowser.data.model.LyricListing
import com.naipofo.utabrowser.data.remote.uta.UtaRepository
import com.naipofo.utabrowser.ui.basicLyricList
import kotlinx.coroutines.launch
import org.kodein.di.compose.localDI
import org.kodein.di.instance

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeRoute(
    showLyric: (url: String) -> Unit,
    goToSearch: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val utaRepository: UtaRepository by localDI().instance()

    var topLyrics: Result<List<LyricListing>>? by remember {
        mutableStateOf(null)
    }

    SideEffect {
        scope.launch {
            topLyrics = utaRepository.getTopSongs()
        }
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { goToSearch() },
                icon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
                text = { Text(text = "Search") }
            )
        },
    ) {
        LazyColumn(Modifier.padding(it)) {
            item {
                Text(
                    text = "Top Lyrics \uD83D\uDD1D",
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    textAlign = TextAlign.Center
                )
            }
            when (val top = topLyrics) {
                is Result.Error -> item {
                    Text(text = "Error loading top lyrics!\n\n${top.exception}")
                }
                is Result.Success -> basicLyricList(
                    lyrics = top.data,
                    onClick = showLyric
                )
                null -> item {
                    Column {
                        CircularProgressIndicator()
                        Text(text = "Loading...")
                    }
                }
            }
        }
    }
}