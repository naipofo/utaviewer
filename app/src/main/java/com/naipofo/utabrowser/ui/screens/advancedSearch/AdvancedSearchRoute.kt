package com.naipofo.utabrowser.ui.screens.advancedSearch

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.naipofo.utabrowser.data.model.LyricsSearchFilters

@Composable
fun AdvancedSearchRoute(
    performSearch: (LyricsSearchFilters) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Advanced Search",
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            textAlign = TextAlign.Center
        )
        val searchFilters = listOf(
            "title",
            "artist",
            "tieup",
            "lyricist",
            "composer",
            "beginning",
            "body"
        ).map {
            Pair(it, searchField(label = it).value.trim())
        }.mapNotNull { if (it.second == "") null else it }
        OutlinedButton(onClick = {
            if (searchFilters.isNotEmpty()) {
                performSearch(LyricsSearchFilters(searchFilters))
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Search")
        }
    }
}

@Composable
fun searchField(label: String): MutableState<String> {
    val textState = remember { mutableStateOf("") }
    var text by textState
    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        singleLine = true,
        label = {
            Text(
                text = label
            )
        })
    return textState
}