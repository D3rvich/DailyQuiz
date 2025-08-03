package ru.d3rvich.result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.d3rvich.result.model.QuizResultUiState
import ru.d3rvich.domain.model.Result
import ru.d3rvich.domain.usecases.GetQuizResultUseCase
import ru.d3rvich.ui.mvibase.BaseViewModel
import ru.d3rvich.ui.mvibase.UiAction
import ru.d3rvich.ui.mvibase.UiEvent
import ru.d3rvich.ui.navigation.Screens
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
internal class QuizResultViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getQuizResultUseCase: Provider<GetQuizResultUseCase>
) : BaseViewModel<QuizResultUiState, UiEvent, UiAction>() {
    override fun createInitialState(): QuizResultUiState = QuizResultUiState.Loading

    override fun obtainEvent(event: UiEvent) {
    }

    init {
        savedStateHandle.toRoute<Screens.QuizResult>().quizId.also { quizId ->
            viewModelScope.launch {
                getQuizResultUseCase.get().invoke(quizId).collect { result ->
                    when (result) {
                        is Result.Success -> setState(QuizResultUiState.Content(result.value))
                        else -> {}
                    }
                }
            }
        }
    }
}