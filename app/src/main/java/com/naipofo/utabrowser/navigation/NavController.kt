package com.naipofo.utabrowser.navigation

import android.util.Log
import androidx.compose.runtime.mutableStateOf

class NavController<DestinationBase>(beginning: DestinationBase) {
    private val backStack = mutableListOf(beginning)
    fun pop() {
        // size == 1 should never happen
        if (canPop) backStack.removeLast()
        currentBackStackEntry.value = backStack.last()
    }

    var currentBackStackEntry = mutableStateOf(beginning)

    val canPop: Boolean
        get() = backStack.size > 1

    fun navigate(destination: DestinationBase) {
        backStack.add(destination)
        currentBackStackEntry.value = destination
    }

    fun absoluteNavigate(destination: DestinationBase) {
        backStack.clear()
        navigate(destination)
    }
}