package ru.d3rvich.database

import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.d3rvich.database.converters.QuestionsConverter
import ru.d3rvich.database.model.AnswerDBO
import ru.d3rvich.database.model.QuestionDBO
import ru.d3rvich.domain.model.Category
import ru.d3rvich.domain.model.Difficulty

@RunWith(AndroidJUnit4::class)
class DatabaseTest {

    private companion object {
        private const val TEST_DB = "migration-test"
    }

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        DailyQuizRoomDatabase::class.java
    )

    @Test
    fun migrate1to2_completesSuccessfully() {
        var db = helper.createDatabase(TEST_DB, 1).apply {
            val answer = AnswerDBO("0", true)
            val question = QuestionDBO(Category.GeneralKnowledge.name, "text", listOf(answer), 0)
            val questionJson = QuestionsConverter().toString(listOf(question))
            execSQL(
                "INSERT INTO quiz (category, difficulty, passed_time, questions) VALUES ('${Category.GeneralKnowledge}', '${Difficulty.AnyDifficulty}', '2025-11-20T16:46:00', '$questionJson')"
            )
        }
        Assert.assertEquals(1, db.version)
        db.close()

        db = helper.runMigrationsAndValidate(TEST_DB, 2, true)
        Assert.assertEquals(2, db.version)
        val cursor = db.query("SELECT correct_answers FROM quiz")
        Assert.assertTrue(cursor.moveToFirst())
        val correctAnswers = cursor.getInt(cursor.getColumnIndex("correct_answers"))
        Assert.assertNotEquals(-1, correctAnswers)
        Assert.assertEquals(1, correctAnswers)
        cursor.close()
        db.close()
    }

    @Test
    fun migrate1to2_correctAnswerCalculation() {
        var db = helper.createDatabase(TEST_DB, 1).apply {
            val answers = List(4) {
                AnswerDBO(it.toString(), it == 2)
            }
            val correctAnswerQuestions = List(3) {
                QuestionDBO(Category.GeneralKnowledge.name, "text", answers, 2)
            }
            val inCorrectAnswerQuestions = List(2) {
                QuestionDBO(Category.GeneralKnowledge.name, "text", answers, 0)
            }
            val questionJson =
                QuestionsConverter().toString(correctAnswerQuestions + inCorrectAnswerQuestions)
            execSQL(
                "INSERT INTO quiz (category, difficulty, passed_time, questions) VALUES ('${Category.GeneralKnowledge}', '${Difficulty.AnyDifficulty}', '1999-06-04T16:46:00', '$questionJson')"
            )
        }
        db.close()

        db = helper.runMigrationsAndValidate(TEST_DB, 2, true)
        val cursor = db.query("SELECT correct_answers FROM quiz")
        Assert.assertTrue(cursor.moveToFirst())
        val correctAnswers = cursor.getInt(cursor.getColumnIndex("correct_answers"))
        cursor.close()
        Assert.assertNotEquals(-1, correctAnswers)
        Assert.assertEquals(3, correctAnswers)
        db.close()
    }

    @Test
    fun migrate1to2_emptyDatabaseTest() {
        var db = helper.createDatabase(TEST_DB, 1)
        db.close()

        db = helper.runMigrationsAndValidate(TEST_DB, 2, true)
        val cursor = db.query("SELECT * FROM quiz")
        Assert.assertEquals(2, db.version)
        Assert.assertFalse(cursor.moveToFirst())
        cursor.close()
        db.close()
    }
}