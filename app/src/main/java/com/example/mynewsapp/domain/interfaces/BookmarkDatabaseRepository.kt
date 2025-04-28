package com.example.mynewsapp.domain.interfaces

import com.example.mynewsapp.data.local.entity.BookmarkEntity

interface BookmarkDatabaseRepository {
    fun readBookmark(): Result<kotlinx.coroutines.flow.Flow<List<BookmarkEntity>>>
    suspend fun writeBookmark(news: BookmarkEntity): Result<Boolean>

    suspend fun deleteBookmark(url: String): Result<Boolean>
    suspend fun isNewsBookmarked(url: String): Result<Boolean>
}