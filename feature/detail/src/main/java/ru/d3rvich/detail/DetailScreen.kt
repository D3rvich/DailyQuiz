package ru.d3rvich.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.d3rvich.detail.model.DetailUiAction
import ru.d3rvich.detail.model.DetailUiEvent
import ru.d3rvich.detail.model.DetailUiState

@Composable
fun DetailScreen(onBackClick: () -> Unit, modifier: Modifier = Modifier) {
    val viewModel: DetailViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    DetailScreen(
        state = state,
        onBackClick = { viewModel.obtainEvent(DetailUiEvent.OnBackClicked) },
        modifier = modifier
    )
    LaunchedEffect(viewModel) {
        viewModel.uiAction.collect { action ->
            when (action) {
                DetailUiAction.NavigateBack -> onBackClick()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DetailScreen(
    state: DetailUiState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (state) {
        DetailUiState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is DetailUiState.Detail -> {
            Scaffold(
                modifier = modifier,
                topBar = {
                    TopAppBar(
                        title = { Text("Detail") },
                        navigationIcon = {
                            IconButton(onClick = onBackClick) {
                                Icon(
                                    Icons.AutoMirrored.Default.ArrowBack,
                                    contentDescription = "Navigate back"
                                )
                            }
                        })
                }) { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    Text(
                        text = "id=${state.entity.id}, text=${state.entity.text}",
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}