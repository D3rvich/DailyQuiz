package ru.d3rvich.ui.model

import androidx.compose.runtime.Immutable
import ru.d3rvich.domain.model.SortBy

@Immutable
sealed class SortByUiModel(val byAscending: Boolean) {
    class Name(byAscending: Boolean) : SortByUiModel(byAscending)
    class PassedTime(byAscending: Boolean) : SortByUiModel(byAscending)
    class CorrectAnswers(byAscending: Boolean) : SortByUiModel(byAscending)
}

fun SortByUiModel.toDomain(): SortBy = when (this) {
    is SortByUiModel.Name -> SortBy.Name(byAscending)
    is SortByUiModel.PassedTime -> SortBy.PassedTime(byAscending)
    is SortByUiModel.CorrectAnswers -> SortBy.CorrectAnswers(byAscending)
}