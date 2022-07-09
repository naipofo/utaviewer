package com.naipofo.utabrowser.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontFamily
import com.naipofo.utabrowser.data.Result
import com.naipofo.utabrowser.data.remote.uta.UtaRepository
import com.naipofo.utabrowser.data.remote.uta.response.LyricElement
import com.naipofo.utabrowser.ui.LyricTile
import kotlinx.coroutines.launch
import org.kodein.di.compose.localDI
import org.kodein.di.instance

@Composable
fun HomeRoute(
    showLyric: (url: String) -> Unit
) {
    val scope = rememberCoroutineScope()
    val utaRepository: UtaRepository by localDI().instance()

    var topLyrics: Result<List<LyricElement>>? by remember {
        mutableStateOf(null)
    }

    SideEffect {
        scope.launch {
            topLyrics = utaRepository.getTopSongs()
        }
    }

    LazyColumn(
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Uta", style= MaterialTheme.typography.displayLarge, fontFamily = FontFamily.Monospace)
                Text(text = "Viewer", style= MaterialTheme.typography.displayLarge, fontFamily = FontFamily.Cursive)
            }
        }
        when (val top = topLyrics){
            is Result.Error -> item{
                Text(text = "Error loading top lyrics!\n\n${top.exception}")
            }
            is Result.Success -> items(top.data){
                LyricTile(data = it, onClick = showLyric)
            }
            null -> item {
                Column {
                    CircularProgressIndicator()
                    Text(text = "Loading...")
                }
            }
        }
    }
}