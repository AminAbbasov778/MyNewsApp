package com.example.mynewsapp.data.repositories

import com.example.mynewsapp.data.local.dao.DeleteBookmarkDao
import com.example.mynewsapp.data.local.dao.IsNewsBookmarkedDao
import com.example.mynewsapp.data.local.dao.ReadBookmarkDao
import com.example.mynewsapp.data.local.dao.WriteBookmarkDao
import com.example.mynewsapp.data.mappers.toData
import com.example.mynewsapp.data.mappers.toDomain
import com.example.mynewsapp.domain.domainmodels.ArticleModel
import com.example.mynewsapp.domain.interfaces.BookmarkDatabaseRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BookmarkDatabaseRepositoryImpl @Inject constructor(
    val readBookmark: ReadBookmarkDao,
    val writeBookmark: WriteBookmarkDao,
    var deleteBookmark: DeleteBookmarkDao,
    var isNewsBookmarked: IsNewsBookmarkedDao,
) : BookmarkDatabaseRepository {


    override fun readBookmark(): Result<kotlinx.coroutines.flow.Flow<List<ArticleModel>>> {
        return try {
            var bookmarks = readBookmark.getBookmark().map { it.map {news -> news.toDomain()  } }
            Result.success(bookmarks)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun writeBookmark(news: ArticleModel): Result<Boolean> {
        return try {
            val bookmarkNews = news.toData()
            writeBookmark.insertBookmark(bookmarkNews)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)

        }


    }

    override suspend fun deleteBookmark(url: String): Result<Boolean> {
        return try {
            deleteBookmark.deleteBookmark(url)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    override suspend fun isNewsBookmarked(url: String): Result<Boolean> {

        return try {
            val isBookmarked = isNewsBookmarked.isBookmarked(url)
            Result.success(isBookmarked)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}