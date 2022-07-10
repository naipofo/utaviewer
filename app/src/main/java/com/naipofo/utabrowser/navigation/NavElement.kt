package com.naipofo.utabrowser.navigation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun <T> NavElement(
    default: T,
    menu: List<VisualNavElement<T>>,
    content: @Composable (T, NavController<T>) -> Unit
) {
    val controller by remember { mutableStateOf(NavController(default)) }
    var selected by remember { mutableStateOf(0) }

    @Composable
    fun display(v: T) = content(v, controller)
    Column {
        Box(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Crossfade(targetState = controller.currentBackStackEntry.value) {
                display(it)
            }
        }
        NavigationBar {
            menu.forEachIndexed { index, item ->
                NavigationBarItem(
                    selected = index == selected,
                    onClick = {
                        controller.absoluteNavigate(item.destination)
                        selected = index
                    },
                    icon = { Icon(imageVector = item.icon, contentDescription = null) },
                    label = { Text(text = item.title) }
                )
            }
        }
    }
}

data class VisualNavElement<T>(
    val icon: ImageVector,
    val title: String,
    val destination: T
)