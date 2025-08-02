package ru.d3rvich.database

import androidx.room.RenameColumn
import androidx.room.migration.AutoMigrationSpec

internal object DatabaseMigrations {

    @RenameColumn(
        tableName = "quiz",
        fromColumnName = "answers",
        toColumnName = "questions"
    )
    class Schema1to2 : AutoMigrationSpec
}