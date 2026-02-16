package ru.d3rvich.ui.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class AnswerUiModel(val text: String, val isCorrect: Boolean)