package ru.d3rvich.database.converters

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import ru.d3rvich.database.model.QuestionDBO

internal class QuestionsConverter {

    @TypeConverter
    fun toString(questions: List<QuestionDBO>): String = Json.encodeToString(questions)

    @TypeConverter
    fun fromString(json: String): List<QuestionDBO> = Json.decodeFromString(json)
}