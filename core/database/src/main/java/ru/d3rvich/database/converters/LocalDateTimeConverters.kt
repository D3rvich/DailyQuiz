package ru.d3rvich.database.converters

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.Json

internal class LocalDateTimeConverters {

    @TypeConverter
    fun toString(dateTime: LocalDateTime): String = Json.encodeToString(dateTime)

    @TypeConverter
    fun fromString(value: String): LocalDateTime =
        Json.decodeFromString(value)
}