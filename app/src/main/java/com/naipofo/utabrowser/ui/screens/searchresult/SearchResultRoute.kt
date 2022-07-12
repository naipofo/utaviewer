package com.naipofo.utabrowser.ui.screens.searchresult

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.naipofo.utabrowser.data.Result
import com.naipofo.utabrowser.data.model.LyricListing
import com.naipofo.utabrowser.data.model.LyricsSearchFilters
import com.naipofo.utabrowser.data.remote.uta.UtaRepository
import com.naipofo.utabrowser.ui.basicLyricList
import kotlinx.coroutines.launch
import org.kodein.di.compose.localDI
import org.kodein.di.instance

@Composable
fun SearchResultRoute(
    query: LyricsSearchFilters, showLyric: (url: String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val utaRepository: UtaRepository by localDI().instance()

    var results: Result<List<LyricListing>>? by remember {
        mutableStateOf(null)
    }

    SideEffect {
        scope.launch { results = utaRepository.searchSong(query) }
    }

    when (val data = results) {
        is Result.Error -> Text("Error searching:\n\n${data.exception}")
        is Result.Success -> LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Text(
                    text = "Search results:",
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
            basicLyricList(data.data, showLyric)
            if (data.data.isEmpty()) item { Text(text = "No results found :(") }
        }
        null -> {
            CircularProgressIndicator()
            Text(text = "Loading...")
        }
    }
}