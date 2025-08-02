package ru.d3rvich.detail.model

import androidx.compose.runtime.Immutable
import ru.d3rvich.domain.entities.QuestionEntity
import ru.d3rvich.domain.entities.QuizEntity
import ru.d3rvich.ui.mvibase.UiState

@Immutable
internal sealed interface QuizUiState : UiState {
    data class Start(val isLoading: Boolean = false) : QuizUiState
    data class Quiz(
        val quiz: QuizEntity,
        val currentQuestion: QuestionEntity,
        val currentQuestionIndex: Int,
    ) : QuizUiState

    data class Results(val correctAnswers: Int, val totalQuestions: Int) : QuizUiState
}