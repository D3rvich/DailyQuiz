package ru.d3rvich.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.datetime.LocalDateTime
import ru.d3rvich.database.converters.CategoryConverters
import ru.d3rvich.database.converters.DifficultConverters
import ru.d3rvich.database.converters.LocalDateTimeConverters
import ru.d3rvich.database.converters.QuestionsConverter
import ru.d3rvich.domain.model.Category
import ru.d3rvich.domain.model.Difficult

@Entity("quiz")
@TypeConverters(
    QuestionsConverter::class,
    LocalDateTimeConverters::class,
    CategoryConverters::class,
    DifficultConverters::class
)
data class QuizDBO(
    @PrimaryKey(autoGenerate = true) @ColumnInfo("id") val id: Long = 0,
    @ColumnInfo("category") val category: Category,
    @ColumnInfo("difficult") val difficult: Difficult,
    @ColumnInfo("passed_time") val passedTime: LocalDateTime,
    @ColumnInfo("questions") val questions: List<QuestionDBO>,
)