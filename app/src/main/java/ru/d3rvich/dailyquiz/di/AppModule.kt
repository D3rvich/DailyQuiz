package ru.d3rvich.dailyquiz.di

import androidx.navigation3.runtime.NavKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import ru.d3rvich.navigation.Navigator

@Module
@InstallIn(ActivityRetainedComponent::class)
internal object AppModule {

    @Provides
    fun provideNavigator(): Navigator = Navigator(object : NavKey {})
}