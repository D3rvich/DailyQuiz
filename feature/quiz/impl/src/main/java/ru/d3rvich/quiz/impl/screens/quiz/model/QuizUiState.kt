package ru.d3rvich.quiz.impl.screens.quiz.model

import ru.d3rvich.quiz.impl.screens.quiz.TimerMaxValue
import ru.d3rvich.ui.model.QuizUiModel
import ru.d3rvich.ui.mvibase.UiState

internal sealed interface QuizUiState : UiState {
    data object Loading : QuizUiState
    data class Quiz(
        val quiz: QuizUiModel,
        val currentQuestionIndex: Int = 0,
        val timer: Long = 0,
        val maxTimerValue: Long = TimerMaxValue,
        val frozen: Boolean = false,
        val showCorrectAnswer: Boolean = false,
        val showTimeout: Boolean = false,
    ) : QuizUiState
}