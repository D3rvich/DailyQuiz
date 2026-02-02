package ru.d3rvich.quiz.impl.screens.quiz

import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import ru.d3rvich.domain.entities.QuizEntity
import ru.d3rvich.domain.entities.QuizResultEntity
import ru.d3rvich.domain.model.Category
import ru.d3rvich.domain.model.Difficulty
import ru.d3rvich.domain.model.Result
import ru.d3rvich.domain.usecases.GetExistedOrNewQuizUseCase
import ru.d3rvich.domain.usecases.SaveQuizUseCase
import ru.d3rvich.quiz.impl.screens.quiz.model.QuizUiAction
import ru.d3rvich.quiz.impl.screens.quiz.model.QuizUiEvent
import ru.d3rvich.quiz.impl.screens.quiz.model.QuizUiState
import ru.d3rvich.quiz.impl.util.Timer
import ru.d3rvich.ui.mappers.toQuestionEntity
import ru.d3rvich.ui.mappers.toQuizUiModel
import ru.d3rvich.ui.model.QuestionUiModel
import ru.d3rvich.ui.model.correctAnswers
import ru.d3rvich.ui.model.isCorrectAnswer
import ru.d3rvich.ui.mvibase.BaseViewModel
import javax.inject.Provider
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@HiltViewModel(assistedFactory = QuizViewModel.Factory::class)
internal class QuizViewModel @AssistedInject constructor(
    @Assisted private val quizId: Long?,
    @Assisted private val category: Category,
    @Assisted private val difficulty: Difficulty,
    private val getExistedOrNewQuizUseCase: Provider<GetExistedOrNewQuizUseCase>,
    private val saveQuizResultUseCase: Provider<SaveQuizUseCase>,
) : BaseViewModel<QuizUiState, QuizUiEvent, QuizUiAction>() {

    init {
        viewModelScope.launch {
            getExistedOrNewQuizUseCase.get().invoke(quizId, category, difficulty)
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

    private val timer by lazy {
        Timer(scope = viewModelScope, onTick = { currentTimer ->
            (currentState as? QuizUiState.Quiz)?.also { quizState ->
                setState(quizState.copy(timer = currentTimer))
            }
        }, onFinish = {
            (currentState as? QuizUiState.Quiz)?.let {
                setState(it.copy(showTimeout = true))
            }
        })
    }

    private fun startQuiz(quizEntity: QuizEntity) {
        setState(QuizUiState.Quiz(quiz = quizEntity.toQuizUiModel()))
        timer.start()
    }

    private suspend fun showCorrectAnswerAndContinue(state: QuizUiState.Quiz) {
        requireNotNull(state.quiz.questions[state.currentQuestionIndex].selectedAnswerIndex)
        setState(state.copy(frozen = true, showCorrectAnswer = true))
        timer.pause()
        delay(2000L)
        if (state.currentQuestionIndex == state.quiz.questions.lastIndex) {
            saveQuizAndShowResult(state, false)
            timer.stop()
        } else {
            val currentTimer = state.timer
            val nextIndex = state.currentQuestionIndex + 1
            if (currentTimer < state.maxTimerValue) {
                setState(state.copy(currentQuestionIndex = nextIndex))
            } else {
                (currentState as? QuizUiState.Quiz)?.let { state ->
                    setState(state.copy(frozen = false))
                }
            }
            timer.resume()
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
                    id = quizId ?: 0,
                    generalCategory = quiz.category,
                    difficulty = quiz.difficulty,
                    passedTime = passedTime,
                    questions = questions.map(QuestionUiModel::toQuestionEntity),
                    correctAnswers = questions.filter { question -> question.isCorrectAnswer }.size
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
                }.toPersistentList()
                val quiz = state.quiz.copy(questions = questions)
                setState(state.copy(quiz = quiz))
            }
        }
    }

    @AssistedFactory
    internal interface Factory {
        fun create(
            quizId: Long?,
            category: Category,
            difficulty: Difficulty
        ): QuizViewModel
    }
}