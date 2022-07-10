package com.naipofo.utabrowser.ui.screens.searchresult

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.naipofo.utabrowser.data.Result
import com.naipofo.utabrowser.data.remote.uta.UtaRepository
import com.naipofo.utabrowser.data.remote.uta.response.LyricElement
import com.naipofo.utabrowser.ui.LyricTile
import kotlinx.coroutines.launch
import org.kodein.di.compose.localDI
import org.kodein.di.instance

@Composable
fun SearchResultRoute(
    query: String, showLyric: (url: String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val utaRepository: UtaRepository by localDI().instance()

    var results: Result<List<LyricElement>>? by remember {
        mutableStateOf(null)
    }

    SideEffect {
        scope.launch { results = utaRepository.searchSong(query) }
    }

    when (val data = results) {
        is Result.Error -> Text("Error searching:\n\n${data.exception}")
        is Result.Success -> LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(data.data) { LyricTile(data = it, onClick = showLyric) }
            if (data.data.isEmpty()) item { Text(text = "No results found :(") }
        }
        null -> {
            CircularProgressIndicator()
            Text(text = "Loading...")
        }
    }
}