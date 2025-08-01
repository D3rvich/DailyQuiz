package ru.d3rvich.network.di

import android.net.http.HttpEngine
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {
    //    fun provideJson(): Json

    @Provides
    fun provideKtorClient(): HttpClient = HttpClient(CIO) {

    }
}