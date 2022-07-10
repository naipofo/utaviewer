package com.naipofo.utabrowser.ui.screens.song

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.naipofo.utabrowser.data.model.YoutubeVideo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YoutubeCard(
    video: YoutubeVideo,
    onClick: () -> Unit
) {
    OutlinedCard(
        Modifier
            .width(200.dp)
            .padding(horizontal = 8.dp)
            .fillMaxHeight()
            .clickable { onClick() }) {
        AsyncImage(
            model = "https://i.ytimg.com/vi/${video.id}/mqdefault.jpg",
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
        )
        Text(
            text = video.title + '\n',
            style = MaterialTheme.typography.labelLarge,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(8.dp)
        )
    }
}