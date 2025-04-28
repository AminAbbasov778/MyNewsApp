package com.example.mynewsapp.di.Module

import com.example.mynewsapp.domain.usecases.commonusecases.GetProcessedNewsUseCase
import com.example.mynewsapp.domain.usecases.search.GetSearchedNewsUseCase
import com.example.mynewsapp.domain.usecases.search.ProcessSearchedNewsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SearchUseCasesModule {


    @Provides
    @Singleton
    fun provideProcessSearchedNews(): ProcessSearchedNewsUseCase {
        return ProcessSearchedNewsUseCase()
    }


    @Provides
    @Singleton
    fun provideGetSearchedNewsUseCase(
        getProcessedNewsUseCase: GetProcessedNewsUseCase,
        processSearchedNewsUseCase: ProcessSearchedNewsUseCase,
    ): GetSearchedNewsUseCase {
        return GetSearchedNewsUseCase(getProcessedNewsUseCase, processSearchedNewsUseCase)
    }

}