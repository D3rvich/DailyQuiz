package ru.d3rvich.debriefing

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.d3rvich.debriefing.model.DebriefingUiState
import ru.d3rvich.domain.model.Result
import ru.d3rvich.domain.usecases.GetQuizResultUseCase
import ru.d3rvich.ui.mvibase.BaseViewModel
import ru.d3rvich.ui.mvibase.UiAction
import ru.d3rvich.ui.mvibase.UiEvent
import ru.d3rvich.ui.navigation.Screens
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
internal class DebriefingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getQuizResultUseCase: Provider<GetQuizResultUseCase>
) : BaseViewModel<DebriefingUiState, UiEvent, UiAction>() {
    override fun createInitialState(): DebriefingUiState = DebriefingUiState.Idle

    override fun obtainEvent(event: UiEvent) {
    }

    init {
        savedStateHandle.toRoute<Screens.QuizDebriefing>().quizId.also { quizId ->
            viewModelScope.launch {
                getQuizResultUseCase.get().invoke(quizId).collect { result ->
                    when (result) {
                        is Result.Success -> setState(DebriefingUiState.Content(result.value))
                        else -> {}
                    }
                }
            }
        }
    }
}