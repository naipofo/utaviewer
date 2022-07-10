package com.naipofo.utabrowser.ui

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.naipofo.utabrowser.navigation.NavElement
import com.naipofo.utabrowser.navigation.VisualNavElement
import com.naipofo.utabrowser.ui.screens.favorites.FavoritesRoute
import com.naipofo.utabrowser.ui.screens.home.HomeRoute
import com.naipofo.utabrowser.ui.screens.searchresult.SearchResultRoute
import com.naipofo.utabrowser.ui.screens.settings.SettingsRoute
import com.naipofo.utabrowser.ui.screens.song.SongRoute

val menu = listOf<VisualNavElement<Destinations>>(
    VisualNavElement(Icons.Default.Home, "Home", Destinations.Home),
    VisualNavElement(Icons.Default.Star, "Favorites", Destinations.Favorites),
    VisualNavElement(Icons.Default.Settings, "Settings", Destinations.Settings)
)

@Composable
fun AppNavigation() {
    val context = LocalContext.current
    NavElement(
        default = Destinations.Home,
        menu = menu
    ) { current, controller ->
        BackHandler(controller.canPop) {
            controller.pop()
        }
        when (current) {
            Destinations.Home -> HomeRoute(
                showLyric = { controller.navigate(Destinations.Song(it)) },
                preformSearch = { controller.navigate(Destinations.SearchResult(it)) }
            )
            Destinations.Favorites -> FavoritesRoute(
                showLyric = { controller.navigate(Destinations.Song(it)) }
            )
            Destinations.Settings -> SettingsRoute()
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
    object Favorites : Destinations
    object Settings : Destinations
    class Song(val url: String) : Destinations
    class SearchResult(val query: String) : Destinations
}