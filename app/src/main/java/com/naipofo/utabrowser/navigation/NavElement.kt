package com.naipofo.utabrowser.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun <T> NavElement(default: T, content: @Composable (T, NavController<T>) -> Unit) {
    val controller by remember { mutableStateOf(NavController(default)) }

    @Composable
    fun display() = content(controller.currentBackStackEntry.value, controller)
    BoxWithConstraints {
        Box(Modifier.fillMaxSize()) {
            display()
        }
    }
}