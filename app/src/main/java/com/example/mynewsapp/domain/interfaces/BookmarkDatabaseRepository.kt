package com.example.mynewsapp.domain.interfaces

import com.example.mynewsapp.data.local.entity.BookmarkEntity
import com.example.mynewsapp.domain.domainmodels.ArticleModel

interface BookmarkDatabaseRepository {
    fun readBookmark(): Result<kotlinx.coroutines.flow.Flow<List<ArticleModel>>>
    suspend fun writeBookmark(news: ArticleModel): Result<Boolean>

    suspend fun deleteBookmark(url: String): Result<Boolean>
    suspend fun isNewsBookmarked(url: String): Result<Boolean>
}