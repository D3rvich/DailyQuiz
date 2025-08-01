package ru.d3rvich.list

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.d3rvich.domain.entities.SampleEntity
import ru.d3rvich.list.model.ListUiAction
import ru.d3rvich.list.model.ListUiEvent
import ru.d3rvich.list.model.ListUiState
import ru.d3rvich.ui.theme.AndroidTemplateTheme

@Composable
fun ListScreen(navigateToDetail: (itemId: Int) -> Unit, modifier: Modifier = Modifier) {
    val viewModel: ListViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    ListScreen(
        state = state,
        modifier = modifier,
        onItemClick = { viewModel.obtainEvent(ListUiEvent.OnItemSelected(it)) }
    )
    LaunchedEffect(viewModel) {
        viewModel.uiAction.collect { action ->
            when (action) {
                is ListUiAction.NavigateToDetail -> navigateToDetail(action.itemId)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ListScreen(
    state: ListUiState,
    onItemClick: (itemId: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    when (state) {
        ListUiState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is ListUiState.Content -> {
            Scaffold(topBar = { TopAppBar(title = { Text("List") }) }) { innerPadding ->
                LazyColumn(
                    modifier = modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {
                    items(state.data, key = { it.id }) { item ->
                        TextButton(
                            onClick = { onItemClick(item.id) }, modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(text = item.text)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ListViewPreview() {
    AndroidTemplateTheme {
        val state = ListUiState.Content(List(15) { SampleEntity(it, it.toString()) })
        ListScreen(state = state, onItemClick = {})
    }
}