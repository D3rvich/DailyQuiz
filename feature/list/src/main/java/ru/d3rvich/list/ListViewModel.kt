package ru.d3rvich.list

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.d3rvich.domain.usecases.GetSamplesUseCase
import ru.d3rvich.list.model.ListUiAction
import ru.d3rvich.list.model.ListUiEvent
import ru.d3rvich.list.model.ListUiState
import ru.d3rvich.ui.mvibase.BaseViewModel
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
internal class ListViewModel @Inject constructor(private val useCase: Provider<GetSamplesUseCase>) :
    BaseViewModel<ListUiState, ListUiEvent, ListUiAction>() {

    init {
        viewModelScope.launch {
            useCase.get().invoke().also { sampleEntities ->
                setState(ListUiState.Content(sampleEntities))
            }
        }
    }

    override fun createInitialState(): ListUiState = ListUiState.Loading

    override fun obtainEvent(event: ListUiEvent) {
        when (event) {
            is ListUiEvent.OnItemSelected -> {
                sendAction {
                    ListUiAction.NavigateToDetail(event.itemId)
                }
            }
        }
    }
}