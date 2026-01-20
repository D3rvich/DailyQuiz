package ru.d3rvich.navigation

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf

val LocalSharedTransitionScope: ProvidableCompositionLocal<SharedTransitionScope> =
    compositionLocalOf { throw IllegalArgumentException("Unexpected access to LocalSharedTransitionScope.") }