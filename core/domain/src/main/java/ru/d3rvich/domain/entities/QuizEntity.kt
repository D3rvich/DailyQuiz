package ru.d3rvich.domain.entities

import ru.d3rvich.domain.model.Category
import ru.d3rvich.domain.model.Difficulty

data class QuizEntity(
    val generalCategory: Category,
    val difficulty: Difficulty,
    val questions: List<QuestionEntity>
)