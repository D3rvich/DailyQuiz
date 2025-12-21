package ru.d3rvich.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey

typealias EntryProviderInstaller = EntryProviderScope<NavKey>.() -> Unit

class Navigator(startDestination: NavKey) {
    val backStack = mutableStateListOf<NavKey>()

    fun goTo(destination: NavKey) {
        backStack.add(destination)
    }

    fun goBack() {
        backStack.removeLastOrNull()
    }

    fun remove(vararg destinations: NavKey) {
        backStack.removeIf { it in destinations }
    }
}