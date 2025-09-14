package ru.d3rvich.database.converters

import androidx.room.TypeConverter
import ru.d3rvich.domain.model.Difficulty

internal class DifficultyConverters {

    @TypeConverter
    fun toString(difficulty: Difficulty): String = difficulty.name

    @TypeConverter
    fun fromString(value: String): Difficulty = Difficulty.valueOf(value)
}