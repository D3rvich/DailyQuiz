package ru.d3rvich.history.model

import androidx.annotation.StringRes
import ru.d3rvich.domain.model.SortBy
import ru.d3rvich.history.R

internal enum class SortByEnum(@param:StringRes val labelTextId: Int) {
    Default(R.string.sort_by_label_default),
    PassedTime(R.string.sort_by_label_passed_time)
}

internal fun SortByEnum.toDomainSortBy(byAscending: Boolean): SortBy = when (this) {
    SortByEnum.Default -> SortBy.Default(byAscending)
    SortByEnum.PassedTime -> SortBy.PassedTime(byAscending)
}

internal fun SortBy.asSortByEnum(): SortByEnum = when (this) {
    is SortBy.Default -> SortByEnum.Default
    is SortBy.PassedTime -> SortByEnum.PassedTime
}
