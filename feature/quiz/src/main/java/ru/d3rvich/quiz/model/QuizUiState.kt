package ru.d3rvich.quiz.model

import ru.d3rvich.domain.entities.QuizEntity
import ru.d3rvich.quiz.TimerMaxValue
import ru.d3rvich.ui.mvibase.UiState

sealed interface QuizUiState : UiState {
    data object Loading : QuizUiState
    data class Quiz(
        val quiz: QuizEntity,
        val currentQuestionIndex: Int = 0,
        val timer: Long = 0,
        val maxTimerValue: Long = TimerMaxValue,
        val frozen: Boolean = false,
        val showCorrectAnswer: Boolean = false,
        val showTimeout: Boolean = false,
    ) : QuizUiState
}