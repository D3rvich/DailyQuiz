package ru.d3rvich.result.impl

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.d3rvich.domain.model.Result
import ru.d3rvich.domain.usecases.GetQuizResultUseCase
import ru.d3rvich.ui.mappers.toQuizResultUiModel
import ru.d3rvich.ui.model.QuizResultUiModel
import javax.inject.Provider

@Stable
@HiltViewModel(assistedFactory = HistoryDetailViewModel.Factory::class)
internal class HistoryDetailViewModel @AssistedInject constructor(
    @Assisted private val quizId: Long,
    private val getQuizResultUseCase: Provider<GetQuizResultUseCase>
) : ViewModel() {

    private val _quizResult: MutableStateFlow<Result<QuizResultUiModel>> =
        MutableStateFlow(Result.Loading)
    val quizResult: StateFlow<Result<QuizResultUiModel>> = _quizResult.asStateFlow()

    init {
        viewModelScope.launch {
            getQuizResultUseCase.get().invoke(quizId).collect { result ->
                when (result) {
                    Result.Loading -> _quizResult.value = Result.Loading
                    is Result.Error -> _quizResult.value = Result.Error(result.exception)
                    is Result.Success ->
                        _quizResult.value = Result.Success(result.value.toQuizResultUiModel())
                }
            }
        }
    }

    @AssistedFactory
    internal interface Factory {
        fun create(quizId: Long): HistoryDetailViewModel
    }
}