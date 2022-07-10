package com.naipofo.utabrowser.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.naipofo.utabrowser.data.model.LyricListing
import com.naipofo.utabrowser.data.remote.uta.response.LyricElement

@Composable
fun LyricTile(data: LyricListing, onClick: (url: String) -> Unit) {
    Row(
        Modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick(data.url) }
    ) {
        AsyncImage(
            model = data.image,
            contentDescription = null,
            modifier = Modifier
                .size(75.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Column(
            Modifier
                .weight(1f)
                .padding(start = 16.dp)
        ) {
            Text(text = data.title, style = MaterialTheme.typography.titleLarge)
            Text(text = data.artist, style = MaterialTheme.typography.titleMedium)
        }
    }
}