package ru.d3rvich.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.d3rvich.database.model.QuizDBO

@Dao
interface QuizDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveQuiz(value: QuizDBO)

    @Query(
        "SELECT * FROM quiz ORDER BY " +
                "CASE WHEN (:sortBy LIKE '${SortByRaw.PASSED_TIME}' AND :isAsc = 1) THEN passed_time END ASC, " +
                "CASE WHEN (:sortBy LIKE '${SortByRaw.PASSED_TIME}' AND :isAsc = 0) THEN passed_time END DESC, " +
                "CASE WHEN (:sortBy LIKE '${SortByRaw.CORRECT_ANSWERS}' AND :isAsc = 1) THEN correct_answers END ASC, " +
                "CASE WHEN (:sortBy LIKE '${SortByRaw.CORRECT_ANSWERS}' AND :isAsc = 0) THEN correct_answers END DESC, " +
                "CASE WHEN (:sortBy LIKE '${SortByRaw.DEFAULT}' AND :isAsc = 1) THEN id END ASC, " +
                "CASE WHEN (:sortBy LIKE '${SortByRaw.DEFAULT}' AND :isAsc = 0) THEN id END DESC"
    )
    fun getQuizHistory(
        sortBy: String = SortByRaw.DEFAULT,
        isAsc: Boolean = true
    ): Flow<List<QuizDBO>>

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