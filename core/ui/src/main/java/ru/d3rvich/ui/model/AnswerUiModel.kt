package ru.d3rvich.ui.model

import androidx.compose.runtime.Immutable

@Immutable
data class AnswerUiModel(val text: String, val isCorrect: Boolean)