package ru.d3rvich.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.datetime.LocalDateTime
import ru.d3rvich.database.converters.CategoryConverters
import ru.d3rvich.database.converters.DifficultyConverters
import ru.d3rvich.database.converters.LocalDateTimeConverters
import ru.d3rvich.database.converters.QuestionsConverter
import ru.d3rvich.domain.model.Category
import ru.d3rvich.domain.model.Difficulty

@Entity("quiz")
@TypeConverters(
    QuestionsConverter::class,
    LocalDateTimeConverters::class,
    CategoryConverters::class,
    DifficultyConverters::class
)
data class QuizDBO(
    @PrimaryKey(autoGenerate = true) @ColumnInfo("id") val id: Long = 0,
    @ColumnInfo("category") val category: Category,
    @ColumnInfo("difficulty") val difficulty: Difficulty,
    @ColumnInfo("passed_time") val passedTime: LocalDateTime,
    @ColumnInfo("questions") val questions: List<QuestionDBO>,
    @ColumnInfo("correct_answers", defaultValue = "-1") val correctAnswers: Int,
)