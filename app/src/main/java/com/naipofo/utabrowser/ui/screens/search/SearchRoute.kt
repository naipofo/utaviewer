package com.naipofo.utabrowser.ui.screens.search

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun SearchRoute(
    performSearch: (String) -> Unit
) {
    Text(text = "Searchin'")
}