package ru.d3rvich.history.impl.screens.history.model

import android.content.Context
import androidx.compose.runtime.Stable
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.d3rvich.domain.model.SortBy
import ru.d3rvich.ui.model.SortByUiModel
import javax.inject.Inject
import javax.inject.Singleton

@Stable
@Singleton
internal class SortByProvider @Inject constructor(@ApplicationContext context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences(SharedPreferencesKey, Context.MODE_PRIVATE)

    private val _currentSortBy: MutableStateFlow<SortByUiModel> = MutableStateFlow(
        sharedPreferences.run {
            val sortByEnum = getString(SortByKey, DefaultSortBy.name)
                ?.let { SortByEnum.valueOf(it) } ?: DefaultSortBy
            val byAscending = getBoolean(ByAscendingKey, true)
            sortByEnum.toUiModel(byAscending)
        })
    val currentValue = _currentSortBy.asStateFlow()

    fun setSortBy(sortBy: SortByUiModel) {
        _currentSortBy.value = sortBy
        sharedPreferences.edit {
            putString(SortByKey, sortBy.toSortByEnum().name)
            putBoolean(ByAscendingKey, sortBy.byAscending)
        }
    }
}

private const val SharedPreferencesKey = "SortByProvider_SharedPreferences"
private const val SortByKey = "SortBy"
private const val ByAscendingKey = "ByAscending"
private val DefaultSortBy = SortByEnum.Name