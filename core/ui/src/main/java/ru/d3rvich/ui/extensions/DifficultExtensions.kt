package ru.d3rvich.ui.extensions

import androidx.annotation.StringRes
import ru.d3rvich.domain.model.Difficulty
import ru.d3rvich.ui.R

@get:StringRes
val Difficulty.stringRes: Int
    get() = when (this) {
        Difficulty.AnyDifficulty -> R.string.difficulty_any
        Difficulty.Easy -> R.string.difficulty_easy
        Difficulty.Medium -> R.string.difficulty_medium
        Difficulty.Hard -> R.string.difficulty_hard
    }
