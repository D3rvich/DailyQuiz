package ru.d3rvich.quiz

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import ru.d3rvich.domain.entities.QuizResultEntity
import ru.d3rvich.domain.entities.correctAnswers
import ru.d3rvich.domain.model.Result
import ru.d3rvich.domain.usecases.GetQuizUseCase
import ru.d3rvich.domain.usecases.SaveQuizUseCase
import ru.d3rvich.quiz.model.QuizUiAction
import ru.d3rvich.quiz.model.QuizUiEvent
import ru.d3rvich.quiz.model.QuizUiState
import ru.d3rvich.ui.mvibase.BaseViewModel
import javax.inject.Inject
import javax.inject.Provider
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@HiltViewModel
internal class QuizViewModel @Inject constructor(
    private val getQuizUseCase: Provider<GetQuizUseCase>,
    private val saveQuizResultUseCase: Provider<SaveQuizUseCase>,
) : BaseViewModel<QuizUiState, QuizUiEvent, QuizUiAction>() {

    override fun createInitialState(): QuizUiState = QuizUiState.Start()

    override fun obtainEvent(event: QuizUiEvent) {
        when (val state = currentState) {
            is QuizUiState.Start -> reduce(state, event)
            is QuizUiState.Quiz -> reduce(state, event)
            is QuizUiState.Results -> reduce(state, event)
        }
    }

    private fun reduce(state: QuizUiState.Start, event: QuizUiEvent) {
        when (event) {
            QuizUiEvent.OnStartClicked -> {
                setState(state.copy(isLoading = true))
                viewModelScope.launch {
                    getQuizUseCase.get().invoke()
                        .flowOn(Dispatchers.IO)
                        .collect { result ->
                            when (result) {
                                Result.Loading -> {
                                    setState(state.copy(isLoading = true))
                                }

                                is Result.Error -> {
                                    sendAction { QuizUiAction.ShowError }
                                    setState(state.copy(isLoading = false))
                                }

                                is Result.Success -> {
                                    setState(
                                        QuizUiState.Quiz(
                                            quiz = result.value,
                                            currentQuestionIndex = 0,
                                            currentQuestion = result.value.questions.first()
                                        )
                                    )
                                }
                            }
                        }
                }
            }

            else -> throw IllegalArgumentException("Unexpected $event for $state")
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun reduce(state: QuizUiState.Quiz, event: QuizUiEvent) {
        when (event) {
            is QuizUiEvent.OnAnswerSelected -> {
                val question = state.currentQuestion.copy(selectedAnswerIndex = event.index)
                val mutableQuestions = state.quiz.questions.toMutableList()
                mutableQuestions[state.currentQuestionIndex] = question
                val quiz = state.quiz.copy(questions = mutableQuestions.toList())
                setState(state.copy(quiz = quiz, currentQuestion = question))
            }

            QuizUiEvent.OnBackClicked -> {
                setState(QuizUiState.Start())
            }

            QuizUiEvent.OnNextClicked -> {
                requireNotNull(state.quiz.questions[state.currentQuestionIndex].selectedAnswerIndex)
                if (state.currentQuestionIndex == state.quiz.questions.lastIndex) {
                    viewModelScope.launch {
                        val passedTime =
                            Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                        val result =
                            state.quiz.let {
                                QuizResultEntity(
                                    generalCategory = it.generalCategory,
                                    passedTime = passedTime,
                                    questions = it.questions
                                )
                            }
                        saveQuizResultUseCase.get().invoke(result)
                    }
                    setState(
                        QuizUiState.Results(
                            correctAnswers = state.quiz.correctAnswers,
                            totalQuestions = state.quiz.questions.size
                        )
                    )
                } else {
                    val nextIndex = state.currentQuestionIndex + 1
                    setState(
                        state.copy(
                            currentQuestionIndex = nextIndex,
                            currentQuestion = state.quiz.questions[nextIndex]
                        )
                    )
                }
            }

            else -> throw IllegalArgumentException("Unexpected $event for $state")
        }
    }

    private fun reduce(state: QuizUiState.Results, event: QuizUiEvent) {
        when (event) {
            QuizUiEvent.OnRetryClicked -> setState(QuizUiState.Start())
            else -> throw IllegalArgumentException("Unexpected $event for $state")
        }
    }
}