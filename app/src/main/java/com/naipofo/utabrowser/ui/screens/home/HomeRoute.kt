package com.naipofo.utabrowser.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
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
    preformSearch: (query: String) -> Unit
) {
    val scope = rememberCoroutineScope()
    val utaRepository: UtaRepository by localDI().instance()

    var topLyrics: Result<List<LyricListing>>? by remember {
        mutableStateOf(null)
    }

    var isSearching by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    SideEffect {
        scope.launch {
            topLyrics = utaRepository.getTopSongs()
        }
    }

    fun doSearch() {
        isSearching = !isSearching
        if (searchQuery.trim() != "") preformSearch(searchQuery)
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    if (!isSearching) Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Uta",
                            style = MaterialTheme.typography.displayLarge,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "Viewer",
                            style = MaterialTheme.typography.displayLarge,
                            fontFamily = FontFamily.Cursive
                        )
                    }
                },
                actions = {
                    if (isSearching) OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        singleLine = true,
                        label = { Text(text = "Search...") },
                        keyboardActions = KeyboardActions {
                            doSearch()
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Search
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                    IconButton(onClick = { doSearch() }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                    }
                }
            )
        },
        content = { innerPadding ->
            LazyColumn(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(innerPadding)
            ) {
                item {
                    Text(text = "Top Lyrics", style = MaterialTheme.typography.displaySmall)
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
    )
}