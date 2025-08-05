package ru.d3rvich.result.model

import ru.d3rvich.ui.mvibase.UiAction

internal sealed interface QuizResultUiAction : UiAction {
    data object ShowErrorAndNavigateToStart : QuizResultUiAction
}