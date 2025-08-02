package ru.d3rvich.history

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.d3rvich.domain.usecases.GetQuizHistoryUseCase
import ru.d3rvich.domain.usecases.RemoveQuizUseCase
import ru.d3rvich.history.model.HistoryUiEvent
import ru.d3rvich.history.model.HistoryUiState
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
        viewModelScope.launch {
            getQuizHistoryUseCase.get().invoke().collect { quizResultEntities ->
                setState(HistoryUiState.Content(quizResultEntities))
            }
        }
    }

    override fun createInitialState(): HistoryUiState = HistoryUiState.Idle

    override fun obtainEvent(event: HistoryUiEvent) {
        require(currentState !is HistoryUiState.Idle) { "Illegal $event for Idle state" }
        when (event) {
            is HistoryUiEvent.OnRemoveQuiz -> viewModelScope.launch {
                removeQuizUseCase.get().invoke(event.quiz)
            }
        }
    }
}