package ru.d3rvich.result.impl

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.d3rvich.domain.model.Result
import ru.d3rvich.result.impl.view.QuizResultDetailView

@Composable
internal fun HistoryDetailScreen(
    quizId: Long,
    navigateToQuiz: (quizId: Long) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HistoryDetailViewModel =
        hiltViewModel<HistoryDetailViewModel, HistoryDetailViewModel.Factory> { factory ->
            factory.create(quizId)
        },
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (val result = viewModel.quizResult.collectAsStateWithLifecycle().value) {
            is Result.Error -> navigateBack()
            Result.Loading -> CircularProgressIndicator()
            is Result.Success -> {
                QuizResultDetailView(
                    modifier = modifier,
                    quizResult = result.value,
                    onRetryClick = navigateToQuiz,
                    onBackClick = navigateBack
                )
            }
        }
    }
}