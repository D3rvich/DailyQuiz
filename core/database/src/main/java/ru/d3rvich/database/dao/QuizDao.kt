package ru.d3rvich.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.d3rvich.database.model.QuizDBO
import ru.d3rvich.domain.model.SortBy

@Dao
interface QuizDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveQuiz(value: QuizDBO)

    fun getQuizHistory(sortBy: SortBy): Flow<List<QuizDBO>> = when (sortBy.byAscending) {
        true -> getQuizHistoryAsc(sortBy.rawValue)
        false -> getQuizHistoryDesc(sortBy.rawValue)
    }

    @Query(
        """
        SELECT * FROM quiz ORDER BY
        CASE 
            WHEN (:sortBy LIKE '${SortByRaw.PASSED_TIME}') THEN passed_time
            WHEN (:sortBy LIKE '${SortByRaw.CORRECT_ANSWERS}') THEN correct_answers 
            WHEN (:sortBy LIKE '${SortByRaw.NAME}') THEN id
        END ASC
    """
    )
    fun getQuizHistoryAsc(sortBy: String): Flow<List<QuizDBO>>

    @Query(
        """
        SELECT * FROM quiz ORDER BY
        CASE 
            WHEN (:sortBy LIKE '${SortByRaw.PASSED_TIME}') THEN passed_time
            WHEN (:sortBy LIKE '${SortByRaw.CORRECT_ANSWERS}') THEN correct_answers 
            WHEN (:sortBy LIKE '${SortByRaw.NAME}') THEN id
        END DESC
    """
    )
    fun getQuizHistoryDesc(sortBy: String): Flow<List<QuizDBO>>

    @Query("SELECT * FROM quiz WHERE id LIKE :id")
    fun getQuizBy(id: Long): Flow<QuizDBO>

    @Delete
    suspend fun removeQuiz(quizDBO: QuizDBO)
}

private object SortByRaw {
    const val NAME = "name"
    const val PASSED_TIME = "passed_time"
    const val CORRECT_ANSWERS = "correct_answers"
}

private val SortBy.rawValue: String
    get() = when (this) {
        is SortBy.Name -> SortByRaw.NAME
        is SortBy.PassedTime -> SortByRaw.PASSED_TIME
        is SortBy.CorrectAnswers -> SortByRaw.CORRECT_ANSWERS
    }