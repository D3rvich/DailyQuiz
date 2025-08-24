package ru.d3rvich.result

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.d3rvich.result.view.QuizResultDetailView
import ru.d3rvich.ui.model.QuizResultUiModel

@Composable
fun QuizResultScreen(
    quizResult: QuizResultUiModel,
    navigateToQuiz: (quizId: Long) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    QuizResultDetailView(
        modifier = modifier,
        quizResult = quizResult,
        onRetryClick = navigateToQuiz,
        onBackClick = navigateBack
    )
}