package ru.d3rvich.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.d3rvich.detail.model.DetailUiAction
import ru.d3rvich.detail.model.DetailUiEvent
import ru.d3rvich.detail.model.DetailUiState
import ru.d3rvich.domain.usecases.GetSampleUseCase
import ru.d3rvich.ui.mvibase.BaseViewModel
import ru.d3rvich.ui.navigation.Screens
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
internal class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getSampleUseCase: Provider<GetSampleUseCase>,
) : BaseViewModel<DetailUiState, DetailUiEvent, DetailUiAction>() {
    override fun createInitialState(): DetailUiState = DetailUiState.Loading

    override fun obtainEvent(event: DetailUiEvent) {
        when (event) {
            DetailUiEvent.OnBackClicked -> sendAction { DetailUiAction.NavigateBack }
        }
    }

    init {
        savedStateHandle.toRoute<Screens.Detail>().id.also { id ->
            viewModelScope.launch {
                val sample = getSampleUseCase.get().invoke(id)
                setState(DetailUiState.Detail(sample))
            }
        }
    }
}