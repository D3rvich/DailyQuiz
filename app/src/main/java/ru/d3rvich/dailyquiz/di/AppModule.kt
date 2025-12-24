package ru.d3rvich.dailyquiz.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import ru.d3rvich.navigation.Navigator
import ru.d3rvich.quiz.api.navigation.Quiz

@Module
@InstallIn(ActivityRetainedComponent::class)
internal object AppModule {

    @Provides
    @ActivityRetainedScoped
    fun provideNavigator(): Navigator = Navigator(Quiz.StartNavKey)
}