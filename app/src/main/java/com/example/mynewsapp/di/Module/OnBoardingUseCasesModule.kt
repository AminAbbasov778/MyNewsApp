package com.example.mynewsapp.di.Module

import com.example.mynewsapp.domain.interfaces.SharedPreferenceRepository
import com.example.mynewsapp.domain.usecases.onboardingusecases.MarkFirstLaunchDoneUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OnBoardingUseCasesModule {

    @Provides
    @Singleton
    fun provideIsFirstLaunchOverUseCase(sharedPreferenceRepository: SharedPreferenceRepository): MarkFirstLaunchDoneUseCase =
        MarkFirstLaunchDoneUseCase(sharedPreferenceRepository)
}