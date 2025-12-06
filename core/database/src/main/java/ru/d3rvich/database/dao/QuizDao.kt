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
            WHEN (:sortBy LIKE '${SortByRaw.DEFAULT}') THEN id
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
            WHEN (:sortBy LIKE '${SortByRaw.DEFAULT}') THEN id
        END DESC
    """
    )
    fun getQuizHistoryDesc(sortBy: String): Flow<List<QuizDBO>>

    @Query("SELECT * FROM quiz WHERE id LIKE :id")
    suspend fun getQuizBy(id: Long): QuizDBO

    @Delete
    suspend fun removeQuiz(quizDBO: QuizDBO)
}

object SortByRaw {
    const val DEFAULT = "default"
    const val PASSED_TIME = "passed_time"
    const val CORRECT_ANSWERS = "correct_answers"
}

private val SortBy.rawValue: String
    get() = when (this) {
        is SortBy.Default -> SortByRaw.DEFAULT
        is SortBy.PassedTime -> SortByRaw.PASSED_TIME
        is SortBy.CorrectAnswers -> SortByRaw.CORRECT_ANSWERS
    }