package ru.d3rvich.database

import androidx.room.migration.AutoMigrationSpec
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.d3rvich.database.converters.QuestionsConverter

internal object DatabaseMigrations {

    class Schema1to2 : AutoMigrationSpec {
        override fun onPostMigrate(db: SupportSQLiteDatabase) {
            val cursor = db.query("SELECT id, questions FROM quiz")
            cursor.use { activeCursor ->
                if (!activeCursor.moveToFirst()) {
                    return@use
                }

                do {
                    val idIndex = activeCursor.getColumnIndex("id")
                    val questionsIndex = activeCursor.getColumnIndex("questions")
                    val quizId = activeCursor.getInt(idIndex)
                    val questions =
                        QuestionsConverter().fromString(cursor.getString(questionsIndex))
                    val correctAnswers =
                        questions.filter { questionDBO -> questionDBO.answers[questionDBO.selectedAnswerIndex].isCorrect }.size
                    db.execSQL(
                        "UPDATE quiz SET correct_answers = ? WHERE id = ?",
                        arrayOf(correctAnswers, quizId)
                    )
                } while (activeCursor.moveToNext())
            }
        }
    }
}