package ru.d3rvich.database.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.d3rvich.database.DailyQuizDatabase
import ru.d3rvich.database.dao.QuizDao

@Module
@InstallIn(SingletonComponent::class)
internal object DaosModule {

    @Provides
    fun provideQuizDao(database: DailyQuizDatabase): QuizDao = database.quizDao()
}