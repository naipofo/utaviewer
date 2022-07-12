package com.naipofo.utabrowser.ui.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.naipofo.utabrowser.data.local.searchSuggestions.SearchSuggestion
import com.naipofo.utabrowser.data.local.searchSuggestions.SearchSuggestionType
import com.naipofo.utabrowser.data.local.searchSuggestions.SearchSuggestionsRepository
import com.naipofo.utabrowser.data.model.LyricsSearchFilters
import org.kodein.di.compose.localDI
import org.kodein.di.instance

@Composable
fun SearchRoute(
    performSearch: (LyricsSearchFilters) -> Unit,
    goToAdvanced: () -> Unit,
    goBack: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    var suggestions: List<SearchSuggestion>? by remember { mutableStateOf(null) }
    val suggestionsRepository: SearchSuggestionsRepository by localDI().instance()
    val focusRequester = remember { FocusRequester() }

    fun refreshSuggestions() {
        if (query.isNotEmpty()) suggestions = suggestionsRepository.getSuggestions(query)
    }

    fun doSearch() {
        if (query.trim() != "") performSearch(LyricsSearchFilters(listOf(Pair("title", query))))
    }

    SideEffect {
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        SmallTopAppBar(
            title = {
                BasicTextField(
                    value = query,
                    onValueChange = {
                        query = it
                        refreshSuggestions()
                    },
                    textStyle = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    decorationBox = {
                        if (query == "") Text(text = "Search...")
                        it()
                    },
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions { doSearch() }
                )
            },
            navigationIcon = {
                IconButton(onClick = { goBack() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Go back")
                }
            },
            actions = {
                IconButton(onClick = { doSearch() }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                }
                IconButton(onClick = { goToAdvanced() }) {
                    Icon(imageVector = Icons.Default.FilterList, contentDescription = "Go back")
                }
            },
            modifier = Modifier.shadow(2.dp)
        )
        suggestions?.forEach {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable {
                        performSearch(
                            LyricsSearchFilters(
                                listOf(
                                    Pair(
                                        when (it.type) {
                                            SearchSuggestionType.SONG -> "title"
                                            SearchSuggestionType.ARTIST -> "artist"
                                        }, it.text
                                    )
                                )
                            )
                        )
                    }
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    modifier = Modifier.padding(16.dp)
                )
                Text(
                    text = it.text,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = .8f)
                )
                Icon(
                    imageVector = when (it.type) {
                        SearchSuggestionType.SONG -> Icons.Default.Album
                        SearchSuggestionType.ARTIST -> Icons.Default.Person
                    },
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = .5f),
                    modifier = Modifier
                        .padding()
                        .padding(16.dp)
                )
            }
        }
    }
}