package ru.d3rvich.quiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import ru.d3rvich.domain.entities.QuizEntity
import ru.d3rvich.domain.entities.QuizResultEntity
import ru.d3rvich.domain.model.Result
import ru.d3rvich.domain.usecases.GetExistedOrNewQuizUseCase
import ru.d3rvich.domain.usecases.SaveQuizUseCase
import ru.d3rvich.quiz.model.QuizUiAction
import ru.d3rvich.quiz.model.QuizUiEvent
import ru.d3rvich.quiz.model.QuizUiState
import ru.d3rvich.ui.mappers.toQuestionEntity
import ru.d3rvich.ui.mappers.toQuizUiModel
import ru.d3rvich.ui.model.QuestionUiModel
import ru.d3rvich.ui.model.correctAnswers
import ru.d3rvich.ui.mvibase.BaseViewModel
import ru.d3rvich.ui.navigation.Screens
import javax.inject.Inject
import javax.inject.Provider
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@HiltViewModel
internal class QuizNewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getExistedOrNewQuizUseCase: Provider<GetExistedOrNewQuizUseCase>,
    private val saveQuizResultUseCase: Provider<SaveQuizUseCase>,
) : BaseViewModel<QuizUiState, QuizUiEvent, QuizUiAction>() {

    private val args = savedStateHandle.toRoute<Screens.QuizMain.Quiz>().also { args ->
        viewModelScope.launch {
            getExistedOrNewQuizUseCase.get().invoke(args.quizId, args.category, args.difficult)
                .collect { result ->
                    when (result) {
                        Result.Loading -> setState(QuizUiState.Loading)

                        is Result.Error -> {
                            sendAction { QuizUiAction.ShowError }
                            sendAction { QuizUiAction.NavigateToStart }
                        }

                        is Result.Success -> startQuiz(result.value)
                    }
                }
        }
    }

    private fun startQuiz(quizEntity: QuizEntity, maxValue: Long = TimerMaxValue) {
        setState(QuizUiState.Quiz(quiz = quizEntity.toQuizUiModel()))
        viewModelScope.launch {
            var currentTimer: Long = 0
            val tick = 1000L
            while (isActive && currentTimer < maxValue) {
                delay(tick)
                currentTimer += tick
                (currentState as? QuizUiState.Quiz)?.also { quizState ->
                    setState(quizState.copy(timer = currentTimer))
                }
            }
            (currentState as? QuizUiState.Quiz)?.also { quizState ->
                setState(quizState.copy(showTimeout = true))
            }
        }
    }

    private suspend fun showCorrectAnswerAndContinue(state: QuizUiState.Quiz) {
        requireNotNull(state.quiz.questions[state.currentQuestionIndex].selectedAnswerIndex)
        setState(state.copy(frozen = true, showCorrectAnswer = true))
        delay(2000L)
        if (state.currentQuestionIndex == state.quiz.questions.lastIndex) {
            saveQuizAndShowResult(state, false)
        } else {
            val currentTimer = (currentState as? QuizUiState.Quiz)?.timer
            val nextIndex = state.currentQuestionIndex + 1
            if ((currentTimer ?: state.timer) < state.maxTimerValue) {
                setState(
                    state.copy(
                        timer = currentTimer ?: state.timer,
                        currentQuestionIndex = nextIndex,
                    )
                )
            } else (currentState as? QuizUiState.Quiz)?.let { state ->
                setState(state.copy(frozen = false))
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun saveQuizAndShowResult(state: QuizUiState.Quiz, hasTimeout: Boolean) {
        viewModelScope.launch {
            val questions = if (hasTimeout) {
                state.quiz.questions.map { question ->
                    if (question.selectedAnswerIndex == null) {
                        val wrongAnswer = question.answers.filterNot { it.isCorrect }.random()
                        val randomWrongAnswerIndex: Int = question.answers.indexOf(wrongAnswer)
                        question.copy(selectedAnswerIndex = randomWrongAnswerIndex)
                    } else question
                }
            } else state.quiz.questions
            val passedTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            val result = state.quiz.let { quiz ->
                QuizResultEntity(
                    id = args.quizId ?: 0,
                    generalCategory = quiz.category,
                    difficult = quiz.difficult,
                    passedTime = passedTime,
                    questions = questions.map(QuestionUiModel::toQuestionEntity)
                )
            }
            saveQuizResultUseCase.get().invoke(result)
            sendAction {
                QuizUiAction.NavigateToResults(
                    state.quiz.correctAnswers,
                    state.quiz.questions.size
                )
            }
        }
    }

    override fun createInitialState(): QuizUiState = QuizUiState.Loading

    override fun obtainEvent(event: QuizUiEvent) {
        when (val state = currentState) {
            QuizUiState.Loading -> return

            is QuizUiState.Quiz -> reduce(state, event)
        }
    }

    private fun reduce(state: QuizUiState.Quiz, event: QuizUiEvent) {
        if (state.frozen) return
        when (event) {
            QuizUiEvent.OnRetryClicked -> {
                saveQuizAndShowResult(state, true)
                setState(state.copy(showTimeout = false))
            }

            QuizUiEvent.OnNextClicked -> viewModelScope.launch {
                showCorrectAnswerAndContinue(state)
            }

            is QuizUiEvent.OnAnswerSelected -> {
                val question =
                    state.quiz.questions[state.currentQuestionIndex].copy(selectedAnswerIndex = event.index)
                val questions = state.quiz.questions.toMutableList().apply {
                    set(state.currentQuestionIndex, question)
                }.toList()
                val quiz = state.quiz.copy(questions = questions)
                setState(state.copy(quiz = quiz))
            }
        }
    }
}

internal const val TimerMaxValue: Long = 5 * 60 * 1000  // 5 minutes