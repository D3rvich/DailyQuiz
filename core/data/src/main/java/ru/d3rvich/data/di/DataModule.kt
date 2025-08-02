package ru.d3rvich.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ru.d3rvich.data.repositories.DailyQuizRepositoryImpl
import ru.d3rvich.database.DailyQuizDatabase
import ru.d3rvich.domain.repositories.DailyQuizRepository
import ru.d3rvich.network.DailyQuizNetworkDataSource

@Module
@InstallIn(ViewModelComponent::class)
internal object DataModule {

    @Provides
    fun provideDailyQuizRepository(
        networkDataSource: DailyQuizNetworkDataSource,
        database: DailyQuizDatabase
    ): DailyQuizRepository =
        DailyQuizRepositoryImpl(networkDataSource = networkDataSource, database = database)
}