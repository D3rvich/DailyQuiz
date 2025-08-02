package ru.d3rvich.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.d3rvich.database.DailyQuizDatabase
import ru.d3rvich.database.DailyQuizRoomDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context): DailyQuizRoomDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            DailyQuizRoomDatabase::class.java,
            "quiz-database"
        ).build()

    @Provides
    @Singleton
    fun provideDailyQuizDatabase(roomDatabase: DailyQuizRoomDatabase) =
        DailyQuizDatabase(roomDatabase)
}