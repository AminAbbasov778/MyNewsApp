package com.example.mynewsapp.di.Module

import com.example.mynewsapp.domain.interfaces.BookmarkDatabaseRepository
import com.example.mynewsapp.domain.usecases.bookmark.ConvertBookmarkEntityToArticleUseCase
import com.example.mynewsapp.domain.usecases.bookmark.ReadBookmarksUseCase
import com.example.mynewsapp.domain.usecases.bookmark.ReverseBookmarkListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BookmarkUseCasesModule {

    @Provides
    @Singleton
    fun getConvertBookmarkEntityToArticleUseCase(): ConvertBookmarkEntityToArticleUseCase {
        return ConvertBookmarkEntityToArticleUseCase()
    }

    @Provides
    @Singleton
    fun getReadBookmarksUseCase(databaseRepository: BookmarkDatabaseRepository): ReadBookmarksUseCase {
        return ReadBookmarksUseCase(databaseRepository)
    }

    @Provides
    @Singleton
    fun getReverseBookmarkListUseCase(): ReverseBookmarkListUseCase {
        return ReverseBookmarkListUseCase()
    }

}