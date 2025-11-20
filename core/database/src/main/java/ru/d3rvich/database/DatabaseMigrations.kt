package ru.d3rvich.database

import androidx.room.migration.AutoMigrationSpec
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.d3rvich.database.converters.QuestionsConverter
import ru.d3rvich.database.model.QuestionDBO

internal object DatabaseMigrations {

    class Schema1to2 : AutoMigrationSpec {
        override fun onPostMigrate(db: SupportSQLiteDatabase) {
            val cursor = db.query("SELECT id, questions FROM quiz")
            val questions = mutableMapOf<Int, List<QuestionDBO>>()
            with(cursor) {
                while (moveToNext()) {
                    val idIndex = cursor.getColumnIndex("id")
                    val questionsIndex = cursor.getColumnIndex("questions")
                    questions[cursor.getInt(idIndex)] =
                        QuestionsConverter().fromString(cursor.getString(questionsIndex))
                }
                close()
            }
            val correctAnswers = mutableMapOf<Int, Int>()
            questions.forEach { (id, questions) ->
                correctAnswers[id] =
                    questions.filter { questionDBO -> questionDBO.answers[questionDBO.selectedAnswerIndex].isCorrect }.size
            }
            correctAnswers.forEach { (id, correctAnswers) ->
                db.execSQL(
                    "UPDATE quiz SET correct_answers = ? WHERE id = ?",
                    arrayOf(correctAnswers, id)
                )
            }
        }
    }
}