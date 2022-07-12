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
import com.naipofo.utabrowser.data.model.LyricsSearchFilters
import com.naipofo.utabrowser.navigation.NavElement
import com.naipofo.utabrowser.navigation.VisualNavElement
import com.naipofo.utabrowser.ui.screens.advancedSearch.AdvancedSearchRoute
import com.naipofo.utabrowser.ui.screens.favorites.FavoritesRoute
import com.naipofo.utabrowser.ui.screens.home.HomeRoute
import com.naipofo.utabrowser.ui.screens.search.SearchRoute
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
                goToSearch = { controller.navigate(Destinations.Search) }
            )
            Destinations.Favorites -> FavoritesRoute(
                showLyric = { controller.navigate(Destinations.Song(it)) }
            )
            Destinations.Settings -> SettingsRoute()
            Destinations.Search -> SearchRoute(
                performSearch = {
                    controller.navigate(
                        Destinations.SearchResult(
                            LyricsSearchFilters(
                                listOf(
                                    Pair("title", it)
                                )
                            )
                        )
                    )
                },
                goToAdvanced = { controller.navigate(Destinations.AdvancedSearch) },
                goBack = { controller.pop() }
            )
            Destinations.AdvancedSearch -> AdvancedSearchRoute {
                controller.absoluteNavigate(Destinations.Home)
                controller.navigate(
                    Destinations.SearchResult(it)
                )
            }
            is Destinations.Song -> SongRoute(
                url = current.url,
                shareLyric = { url, title ->
                    context.startActivity(
                        Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, title)
                            putExtra(Intent.EXTRA_TEXT, url)
                        }
                    )
                },
                openYoutubeVideo = { id: String ->
                    context.startActivity(
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=$id"))
                    )
                },
                goBack = { controller.pop() }
            )
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
    object Search : Destinations
    object AdvancedSearch : Destinations
    class Song(val url: String) : Destinations
    class SearchResult(val query: LyricsSearchFilters) : Destinations
}