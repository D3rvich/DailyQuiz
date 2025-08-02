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

    @Query("SELECT * FROM quiz")
    fun getQuizHistory(): Flow<List<QuizDBO>>

    @Query("SELECT * FROM quiz WHERE id LIKE :id")
    suspend fun getQuizBy(id: Long): QuizDBO

    @Delete
    suspend fun removeQuiz(quizDBO: QuizDBO)
}