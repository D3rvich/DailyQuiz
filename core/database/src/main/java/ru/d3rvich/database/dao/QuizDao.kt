package ru.d3rvich.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.d3rvich.database.model.QuizDBO

@Dao
interface QuizDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveQuiz(value: QuizDBO)

    @Query("SELECT * FROM quiz")
    suspend fun getQuizList(): List<QuizDBO>

    @Query("SELECT * FROM quiz WHERE id LIKE :id")
    suspend fun getQuizBy(id: Long): QuizDBO
}