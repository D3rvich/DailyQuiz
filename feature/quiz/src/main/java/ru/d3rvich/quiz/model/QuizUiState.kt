package ru.d3rvich.quiz.model

import androidx.compose.runtime.Immutable
import ru.d3rvich.domain.entities.QuestionEntity
import ru.d3rvich.domain.entities.QuizEntity
import ru.d3rvich.domain.model.Category
import ru.d3rvich.domain.model.Difficult
import ru.d3rvich.ui.mvibase.UiState

@Immutable
internal sealed interface QuizUiState : UiState {
    data class Start(val isLoading: Boolean = false) : QuizUiState

    data class Filters(
        val isLoading: Boolean = false,
        val selectedCategory: Category? = null,
        val selectedDifficult: Difficult? = null,
    ) : QuizUiState

    data class Quiz(
        val quiz: QuizEntity,
        val currentQuestion: QuestionEntity = quiz.questions.first(),
        val currentQuestionIndex: Int = 0,
        val showCorrectAnswer: Boolean = false,
        val timerCurrentValue: Long = 0,
        val timerMaxValue: Long = TimerMaxValue,
        val showTimeout: Boolean = false,
        val frozen: Boolean = false,
    ) : QuizUiState

    data class Results(val correctAnswers: Int, val totalQuestions: Int) : QuizUiState
}

internal const val TimerMaxValue: Long = 5 * 60 * 1000  // 5 minutes