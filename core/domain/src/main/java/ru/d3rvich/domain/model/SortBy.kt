package ru.d3rvich.domain.model

sealed class SortBy(val byAscending: Boolean) {
    class Name(byAscending: Boolean) : SortBy(byAscending)
    class PassedTime(byAscending: Boolean) : SortBy(byAscending)
    class CorrectAnswers(byAscending: Boolean) : SortBy(byAscending)
}