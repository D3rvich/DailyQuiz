package ru.d3rvich.history

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.d3rvich.domain.entities.QuizResultEntity
import ru.d3rvich.history.model.HistoryUiEvent
import ru.d3rvich.history.model.HistoryUiState
import ru.d3rvich.ui.theme.DailyQuizTheme

@Composable
fun HistoryScreen(
    navigateToQuiz: () -> Unit,
    navigateToQuizDebriefing: (quizId: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: HistoryViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    HistoryScreen(
        state = state,
        modifier = modifier,
        onRemoveQuiz = { viewModel.obtainEvent(HistoryUiEvent.OnRemoveQuiz(it)) },
        onStartQuizClick = { navigateToQuiz() },
        onQuizClick = { navigateToQuizDebriefing(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HistoryScreen(
    state: HistoryUiState,
    onStartQuizClick: () -> Unit,
    onQuizClick: (quizId: Long) -> Unit,
    onRemoveQuiz: (quiz: QuizResultEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    when (state) {
        HistoryUiState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is HistoryUiState.Content -> {
            Scaffold(topBar = { TopAppBar(title = { Text("List") }) }) { innerPadding ->
                LazyColumn(
                    modifier = modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {
                    items(state.data, key = { it.id }) { item ->
                        TextButton(
                            onClick = { onQuizClick(item.id) }, modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(text = item.title)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HistoryPreview() {
    DailyQuizTheme {
//        val state = ListUiState.Content(List(15) { QuestionEntity(it, it.toString()) })
//        ListScreen(state = state, onItemClick = {})
    }
}