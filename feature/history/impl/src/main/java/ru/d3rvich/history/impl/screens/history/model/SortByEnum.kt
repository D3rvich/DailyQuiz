package ru.d3rvich.history.impl.screens.history.model

import androidx.annotation.StringRes
import ru.d3rvich.domain.model.SortBy
import ru.d3rvich.history.impl.R
import ru.d3rvich.ui.model.SortByUiModel

internal enum class SortByEnum(@param:StringRes val labelTextId: Int) {
    Name(R.string.sort_by_label_name),
    PassedTime(R.string.sort_by_label_passed_time),
    CorrectAnswers(R.string.sort_by_label_correct_answers)
}

internal fun SortByEnum.toUiModel(byAscending: Boolean = defaultAscending()): SortByUiModel =
    when (this) {
        SortByEnum.Name -> SortByUiModel.Name(byAscending)
        SortByEnum.PassedTime -> SortByUiModel.PassedTime(byAscending)
        SortByEnum.CorrectAnswers -> SortByUiModel.CorrectAnswers(byAscending)
    }

internal fun SortByUiModel.toSortByEnum(): SortByEnum = when (this) {
    is SortByUiModel.Name -> SortByEnum.Name
    is SortByUiModel.PassedTime -> SortByEnum.PassedTime
    is SortByUiModel.CorrectAnswers -> SortByEnum.CorrectAnswers
}

internal fun SortBy.toSortByEnum(): SortByEnum = when (this) {
    is SortBy.Name -> SortByEnum.Name
    is SortBy.PassedTime -> SortByEnum.PassedTime
    is SortBy.CorrectAnswers -> SortByEnum.CorrectAnswers
}

private fun SortByEnum.defaultAscending(): Boolean = when (this) {
    SortByEnum.CorrectAnswers -> false
    else -> true
}