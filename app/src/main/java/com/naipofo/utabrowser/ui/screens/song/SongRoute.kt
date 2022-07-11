package com.naipofo.utabrowser.ui.screens.song

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import com.naipofo.utabrowser.data.Result
import com.naipofo.utabrowser.data.dataOrNull
import com.naipofo.utabrowser.data.local.favorites.FavoritesRepository
import com.naipofo.utabrowser.data.local.settings.SettingsRepository
import com.naipofo.utabrowser.data.model.LyricPage
import com.naipofo.utabrowser.data.remote.uta.UtaRepository
import kotlinx.coroutines.launch
import org.kodein.di.compose.localDI
import org.kodein.di.instance

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SongRoute(
    url: String,
    openYoutubeVideo: (id: String) -> Unit,
    shareLyric: (url: String, title: String) -> Unit,
    goBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val utaRepository: UtaRepository by localDI().instance()
    val settingsRepository: SettingsRepository by localDI().instance()
    val favoritesRepository: FavoritesRepository by localDI().instance()
    val clipboardManager =
        LocalContext.current.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    var lyricData: Result<LyricPage>? by remember {
        mutableStateOf(null)
    }
    var isFavorite by remember {
        mutableStateOf(false)
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        rememberTopAppBarScrollState()
    )
    val snackbarHostState = remember { SnackbarHostState() }

    SideEffect {
        scope.launch {
            lyricData = utaRepository.getLyricPage(url)
            isFavorite = favoritesRepository.isFavorite(url)
        }
    }

    fun toggleFavorite() {
        isFavorite = !isFavorite
        scope.launch { favoritesRepository.setFavorite(url, isFavorite) }
    }

    fun copyTitle() {
        scope.launch {
            lyricData?.dataOrNull()?.listing?.let {
                clipboardManager.setPrimaryClip(
                    ClipData.newPlainText("song title", "${it.artist} - ${it.title}")
                )
                snackbarHostState.showSnackbar("Copied title to the clipboard")
            }
        }
    }

    Scaffold(
        modifier = Modifier,
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(
                        text = lyricData?.dataOrNull()?.listing?.let {
                            AnnotatedString.Builder().apply {
                                append(it.title)
                                append("  ")
                                pushStyle(
                                    SpanStyle(
                                        color = MaterialTheme.colorScheme.onSurface.copy(
                                            alpha = .8f
                                        )
                                    )
                                )
                                append(it.artist)
                            }
                        }?.toAnnotatedString() ?: AnnotatedString(""),
                        modifier = Modifier.combinedClickable(
                            onClick = {},
                            onLongClick = { copyTitle() }
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { goBack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "go back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        lyricData?.dataOrNull()?.apply {
                            shareLyric(url, listing.title)
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share"
                        )
                    }
                    IconToggleButton(checked = isFavorite, onCheckedChange = {
                        toggleFavorite()
                    }) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.Star,
                            contentDescription = "Favorite lyric"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) {
        LazyColumn(
            Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            when (val data = lyricData) {
                is Result.Error -> {
                    item { Text(text = "Error!:\n\n${data.exception}\n${data.exception.stackTrace}") }
                    throw data.exception
                }
                is Result.Success -> {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            data.data.youtubeVideos.forEach {
                                YoutubeCard(video = it) {
                                    openYoutubeVideo(it.id)
                                }
                            }
                        }
                    }
                    rubyRender(
                        lyrics = data.data.text,
                        sizeMultiplier = settingsRepository.sizeMultiplier,
                        showRuby = settingsRepository.showRuby
                    )
                }
                null -> item {
                    CircularProgressIndicator()
                    Text(text = "Loading...")
                }
            }
        }
    }
}
