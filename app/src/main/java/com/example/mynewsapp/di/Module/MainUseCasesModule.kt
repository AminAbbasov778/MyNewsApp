package com.example.mynewsapp.di.Module

import com.example.mynewsapp.domain.interfaces.SharedPreferenceRepository
import com.example.mynewsapp.domain.usecases.mainusecases.IsFirstLaunchUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainUseCasesModule {

    @Provides
    @Singleton
    fun provideIsFirstLaunchUseCase(sharedPreferenceRepository: SharedPreferenceRepository): IsFirstLaunchUseCase =
        IsFirstLaunchUseCase(sharedPreferenceRepository)
}