package ru.d3rvich.history

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.d3rvich.domain.entities.QuizResultEntity
import ru.d3rvich.domain.model.SortBy
import ru.d3rvich.domain.usecases.GetQuizHistoryUseCase
import ru.d3rvich.domain.usecases.RemoveQuizUseCase
import ru.d3rvich.history.model.HistoryUiEvent
import ru.d3rvich.history.model.HistoryUiState
import ru.d3rvich.ui.mappers.toQuizResultEntity
import ru.d3rvich.ui.mappers.toQuizResultUiModel
import ru.d3rvich.ui.mvibase.BaseViewModel
import ru.d3rvich.ui.mvibase.UiAction
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
internal class HistoryViewModel @Inject constructor(
    private val getQuizHistoryUseCase: Provider<GetQuizHistoryUseCase>,
    private val removeQuizUseCase: Provider<RemoveQuizUseCase>,
) : BaseViewModel<HistoryUiState, HistoryUiEvent, UiAction>() {

    init {
        setUpHistory(sortBy = SortBy.Default(true))
    }

    override fun createInitialState(): HistoryUiState = HistoryUiState.Loading

    override fun obtainEvent(event: HistoryUiEvent) {
        require(currentState !is HistoryUiState.Loading) { "Illegal $event for $currentState state" }
        when (event) {
            is HistoryUiEvent.OnRemoveQuiz -> viewModelScope.launch {
                removeQuizUseCase.get().invoke(event.quiz.toQuizResultEntity())
            }

            is HistoryUiEvent.OnSortChange -> {
                setUpHistory(event.selectedSort)
            }
        }
    }

    private fun setUpHistory(sortBy: SortBy) {
        viewModelScope.launch {
            getQuizHistoryUseCase.get().invoke(sortBy = sortBy).collect { quizResults ->
                setState(
                    HistoryUiState.Content(
                        quizResultEntities = quizResults.map(QuizResultEntity::toQuizResultUiModel),
                        selectedSort = sortBy
                    )
                )
            }
        }
    }
}