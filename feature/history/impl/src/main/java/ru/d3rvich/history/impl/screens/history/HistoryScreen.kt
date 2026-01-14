package ru.d3rvich.history.impl.screens.history

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.d3rvich.history.impl.screens.history.model.HistoryUiEvent
import ru.d3rvich.history.impl.screens.history.model.HistoryUiState
import ru.d3rvich.history.impl.views.QuizHistoryTopAppBar
import ru.d3rvich.history.impl.views.QuizHistoryView
import ru.d3rvich.history.impl.views.animateColor
import ru.d3rvich.ui.model.QuizResultUiModel
import ru.d3rvich.ui.model.SortByUiModel

@Composable
internal fun HistoryScreen(
    navigateToEmptyHistory: () -> Unit,
    navigateToQuizResult: (quizId: Long) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: HistoryViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    HistoryScreen(
        state = state,
        modifier = modifier,
        onRemoveQuiz = { viewModel.obtainEvent(HistoryUiEvent.OnRemoveQuiz(it)) },
        onSortChange = { selectedSort ->
            viewModel.obtainEvent(HistoryUiEvent.OnSortChange(selectedSort))
        },
        onQuizClick = { navigateToQuizResult(it) },
        onEmptyHistory = navigateToEmptyHistory,
        onBackClick = navigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HistoryScreen(
    state: HistoryUiState,
    onSortChange: (selectedSort: SortByUiModel) -> Unit,
    onQuizClick: (quizId: Long) -> Unit,
    onRemoveQuiz: (quizResult: QuizResultUiModel) -> Unit,
    onEmptyHistory: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(canScroll = { state is HistoryUiState.Content })

    var isAnyItemSelected by remember { mutableStateOf(false) }
    val animateColor = animateColor(isAnyItemSelected)

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            QuizHistoryTopAppBar(
                selectedSort = state.selectedSort,
                scrollBehavior = scrollBehavior,
                onSortChange = onSortChange,
                onBackClick = onBackClick,
                modifier = Modifier.drawWithContent {
                    drawContent()
                    drawRect(animateColor)
                }
            )
        },
        contentWindowInsets = WindowInsets()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when (state) {
                is HistoryUiState.Loading -> {
                    CircularProgressIndicator()
                }

                is HistoryUiState.Content -> {
                    if (state.quizResultEntities.isEmpty()) {
                        LaunchedEffect(Unit) { onEmptyHistory() }
                    }

                    QuizHistoryView(
                        quizList = state.quizResultEntities,
                        onQuizCLick = onQuizClick,
                        onRemoveQuiz = onRemoveQuiz,
                        onSelectionChange = { isAnyItemSelected = it }
                    )
                }
            }
        }
    }
}