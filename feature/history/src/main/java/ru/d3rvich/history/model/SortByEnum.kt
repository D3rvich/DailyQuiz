package ru.d3rvich.history.model

import androidx.annotation.StringRes
import ru.d3rvich.domain.model.SortBy
import ru.d3rvich.domain.model.SortBy.*
import ru.d3rvich.history.R

internal enum class SortByEnum(@param:StringRes val labelTextId: Int) {
    Name(R.string.sort_by_label_name),
    PassedTime(R.string.sort_by_label_passed_time),
    CorrectAnswers(R.string.sort_by_label_correct_answers)
}

internal fun SortByEnum.toDomainSortBy(byAscending: Boolean = defaultAscending()): SortBy =
    when (this) {
        SortByEnum.Name -> Name(byAscending)
        SortByEnum.PassedTime -> PassedTime(byAscending)
        SortByEnum.CorrectAnswers -> CorrectAnswers(byAscending)
    }

internal fun SortBy.asSortByEnum(): SortByEnum = when (this) {
    is Name -> SortByEnum.Name
    is PassedTime -> SortByEnum.PassedTime
    is CorrectAnswers -> SortByEnum.CorrectAnswers
}

private fun SortByEnum.defaultAscending(): Boolean = when (this) {
    SortByEnum.CorrectAnswers -> false
    else -> true
}