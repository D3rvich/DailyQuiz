package ru.d3rvich.quiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import ru.d3rvich.domain.entities.QuestionEntity
import ru.d3rvich.domain.entities.QuizEntity
import ru.d3rvich.domain.entities.QuizResultEntity
import ru.d3rvich.domain.entities.correctAnswers
import ru.d3rvich.domain.model.Result
import ru.d3rvich.domain.usecases.GetQuizResultUseCase
import ru.d3rvich.domain.usecases.GetQuizUseCase
import ru.d3rvich.domain.usecases.SaveQuizUseCase
import ru.d3rvich.quiz.model.QuizUiAction
import ru.d3rvich.quiz.model.QuizUiEvent
import ru.d3rvich.quiz.model.QuizUiState
import ru.d3rvich.ui.mvibase.BaseViewModel
import ru.d3rvich.ui.navigation.Screens
import javax.inject.Inject
import javax.inject.Provider
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@HiltViewModel
internal class QuizViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getQuizUseCase: Provider<GetQuizUseCase>,
    private val saveQuizResultUseCase: Provider<SaveQuizUseCase>,
    private val getQuizResultUseCase: Provider<GetQuizResultUseCase>,
) : BaseViewModel<QuizUiState, QuizUiEvent, QuizUiAction>() {

    override fun createInitialState(): QuizUiState = QuizUiState.Start(quizId != null)

    override fun obtainEvent(event: QuizUiEvent) {
        when (val state = currentState) {
            is QuizUiState.Start -> reduce(state, event)
            is QuizUiState.Quiz -> reduce(state, event)
            is QuizUiState.Results -> reduce(state, event)
        }
    }

    private var quizId: Long? =
        savedStateHandle.toRoute<Screens.Quiz>().repeatQuizId.also { quizId ->
            quizId?.let { getQuizResult(quizId) }
        }

    private fun reduce(state: QuizUiState.Start, event: QuizUiEvent) {
        when (event) {
            QuizUiEvent.OnStartClicked -> {
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
                                        QuizUiState.Quiz(quiz = result.value)
                                    )
                                }
                            }
                        }
                }
            }

            QuizUiEvent.EnterScreen -> {
                quizId?.let { quizId -> getQuizResult(quizId) }
            }

            else -> throw IllegalArgumentException("Unexpected $event for $state")
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun reduce(state: QuizUiState.Quiz, event: QuizUiEvent) {
        when (event) {
            is QuizUiEvent.EnterScreen -> { // Ignore
            }

            is QuizUiEvent.OnAnswerSelected -> {
                val question = state.currentQuestion.copy(selectedAnswerIndex = event.index)
                val mutableQuestions = state.quiz.questions.toMutableList()
                mutableQuestions[state.currentQuestionIndex] = question
                val quiz = state.quiz.copy(questions = mutableQuestions.toList())
                setState(state.copy(quiz = quiz, currentQuestion = question))
            }

            QuizUiEvent.OnBackClicked -> {
                quizId = null
                setState(QuizUiState.Start())
            }

            QuizUiEvent.OnNextClicked -> {
                requireNotNull(state.quiz.questions[state.currentQuestionIndex].selectedAnswerIndex)
                if (state.currentQuestionIndex == state.quiz.questions.lastIndex) {
                    saveQuiz(state)
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
            is QuizUiEvent.EnterScreen -> { // Ignore
            }

            QuizUiEvent.OnRetryClicked -> {
                quizId = null
                setState(QuizUiState.Start())
            }

            else -> throw IllegalArgumentException("Unexpected $event for $state")
        }
    }

    private fun getQuizResult(quizId: Long) {
        viewModelScope.launch {
            getQuizResultUseCase.get().invoke(quizId).collect { result ->
                when (result) {
                    is Result.Error -> {
                        sendAction { QuizUiAction.ShowError }
                        setState(QuizUiState.Start())
                    }

                    Result.Loading -> QuizUiState.Start(true)
                    is Result.Success -> {
                        val quiz = result.value.toQuizEntity()
                        setState(QuizUiState.Quiz(quiz))
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun saveQuiz(state: QuizUiState.Quiz) {
        viewModelScope.launch {
            val passedTime =
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            val result =
                state.quiz.let {
                    QuizResultEntity(
                        id = quizId ?: 0,
                        generalCategory = it.generalCategory,
                        passedTime = passedTime,
                        questions = it.questions
                    )
                }
            saveQuizResultUseCase.get().invoke(result)
        }
    }
}

private fun QuizResultEntity.toQuizEntity(): QuizEntity =
    QuizEntity(generalCategory, questions.map { it.disableAnswers() })

private fun QuestionEntity.disableAnswers(): QuestionEntity = copy(selectedAnswerIndex = null)