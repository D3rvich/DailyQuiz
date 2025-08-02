package ru.d3rvich.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.datetime.LocalDateTime
import ru.d3rvich.database.converters.LocalDateTimeConverters
import ru.d3rvich.database.converters.QuestionsConverter

@Entity("quiz")
@TypeConverters(QuestionsConverter::class, LocalDateTimeConverters::class)
data class QuizDBO(
    @PrimaryKey(autoGenerate = true) @ColumnInfo("id") val id: Long = 0,
    @ColumnInfo("title") val title: String,
    @ColumnInfo("category") val category: String,
    @ColumnInfo("passed_time") val passedTime: LocalDateTime,
    @ColumnInfo("questions") val questions: List<QuestionDBO>,
)