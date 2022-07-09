package com.naipofo.utabrowser.ui

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import com.naipofo.utabrowser.navigation.NavElement
import com.naipofo.utabrowser.ui.screens.home.HomeRoute
import com.naipofo.utabrowser.ui.screens.song.SongRoute

@Composable
fun AppNavigation() {
    NavElement<Destinations>(
        Destinations.Home,
    ) { current, controller ->
        BackHandler(controller.canPop) {
            controller.pop()
        }
        when (current) {
            Destinations.Home -> HomeRoute {
                controller.navigate(Destinations.Song(it))
            }
            is Destinations.Song -> SongRoute(current.url)
        }
    }
}

sealed interface Destinations {
    object Home : Destinations
    class Song(val url: String): Destinations
}