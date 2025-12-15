package ru.d3rvich.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import ru.d3rvich.database.dao.QuizDao
import ru.d3rvich.database.model.QuizDBO

@Database(
    entities = [QuizDBO::class],
    version = 2,
    autoMigrations = [
        AutoMigration(from = 1, to = 2, spec = DatabaseMigrations.Schema1to2::class)
    ],
    exportSchema = true
)
internal abstract class DailyQuizDatabase : RoomDatabase() {
    abstract fun quizDao(): QuizDao
}