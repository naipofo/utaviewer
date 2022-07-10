package com.naipofo.utabrowser.ui

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.naipofo.utabrowser.navigation.NavElement
import com.naipofo.utabrowser.ui.screens.home.HomeRoute
import com.naipofo.utabrowser.ui.screens.searchresult.SearchResultRoute
import com.naipofo.utabrowser.ui.screens.song.SongRoute

@Composable
fun AppNavigation() {
    val context = LocalContext.current
    NavElement<Destinations>(
        Destinations.Home,
    ) { current, controller ->
        BackHandler(controller.canPop) {
            controller.pop()
        }
        when (current) {
            Destinations.Home -> HomeRoute(
                showLyric = { controller.navigate(Destinations.Song(it)) },
                preformSearch = { controller.navigate(Destinations.SearchResult(it)) }
            )
            is Destinations.Song -> SongRoute(current.url) { id: String ->
                context.startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=$id"))
                )
            }
            is Destinations.SearchResult -> SearchResultRoute(current.query) {
                controller.navigate(
                    Destinations.Song(it)
                )
            }
        }
    }
}

sealed interface Destinations {
    object Home : Destinations
    class Song(val url: String) : Destinations
    class SearchResult(val query: String) : Destinations
}