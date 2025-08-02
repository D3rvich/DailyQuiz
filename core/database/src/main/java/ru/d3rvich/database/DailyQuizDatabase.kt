package ru.d3rvich.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.d3rvich.database.dao.QuizDao
import ru.d3rvich.database.model.QuizDBO

class DailyQuizDatabase internal constructor(private val database: DailyQuizRoomDatabase) {

    val quizDao: QuizDao
        get() = database.quizDao()
}

@Database(
    entities = [QuizDBO::class],
    version = 1,
    exportSchema = false,
)
internal abstract class DailyQuizRoomDatabase : RoomDatabase() {
    abstract fun quizDao(): QuizDao
}