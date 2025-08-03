package ru.d3rvich.database.converters

import androidx.room.TypeConverter
import ru.d3rvich.domain.model.Difficult

internal class DifficultConverters {

    @TypeConverter
    fun toString(category: Difficult): String = category.name

    @TypeConverter
    fun fromString(value: String): Difficult = Difficult.valueOf(value)
}