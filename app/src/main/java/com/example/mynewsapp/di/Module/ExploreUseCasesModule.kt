package com.example.mynewsapp.di.Module

import com.example.mynewsapp.domain.interfaces.TopicRepository
import com.example.mynewsapp.domain.usecases.exploreusecases.GetTopicsUseCases
import com.example.mynewsapp.domain.usecases.exploreusecases.GetSavedTopicsUseCase
import com.example.mynewsapp.domain.usecases.exploreusecases.SaveTopicUseCase
import com.example.mynewsapp.domain.usecases.exploreusecases.UnSaveTopicUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent :: class)
object ExploreUseCasesModule {

    @Provides
    @Singleton
    fun provideGetTopicsUseCases(topicRepository: TopicRepository):GetTopicsUseCases = GetTopicsUseCases(topicRepository)

    @Provides
    @Singleton
    fun provideIsTopicSavedUseCase(topicRepository: TopicRepository):GetSavedTopicsUseCase = GetSavedTopicsUseCase(topicRepository)

    @Provides
    @Singleton
    fun provideSaveTopicUseCase(topicRepository: TopicRepository):SaveTopicUseCase = SaveTopicUseCase(topicRepository)

    @Provides
    @Singleton
    fun provideUnSaveTopicUseCase(topicRepository: TopicRepository):UnSaveTopicUseCase = UnSaveTopicUseCase(topicRepository)


}