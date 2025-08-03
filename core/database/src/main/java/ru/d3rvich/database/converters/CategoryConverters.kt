package ru.d3rvich.database.converters

import androidx.room.TypeConverter
import ru.d3rvich.domain.model.Category

internal class CategoryConverters {

    @TypeConverter
    fun toString(category: Category): String = category.name

    @TypeConverter
    fun fromString(value: String): Category = Category.valueOf(value)
}