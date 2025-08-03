package ru.d3rvich.domain.model

enum class Difficult(val code: String, val text: String) {
    AnyDifficulty("Any", "Any Difficulty"),
    Easy("easy", "Easy"),
    Medium("medium", "Medium"),
    Hard("hard", "Hard")
}