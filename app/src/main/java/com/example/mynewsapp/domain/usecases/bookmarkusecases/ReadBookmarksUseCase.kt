package com.example.mynewsapp.domain.usecases.bookmark

import com.example.mynewsapp.data.local.entity.BookmarkEntity
import com.example.mynewsapp.domain.interfaces.BookmarkDatabaseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReadBookmarksUseCase @Inject constructor(
    private val databaseRepository: BookmarkDatabaseRepository,
    private val reverseBookmarkListUseCase: ReverseBookmarkListUseCase,
) {
    operator fun invoke(): Result<Flow<List<BookmarkEntity>>> {
        val result = databaseRepository.readBookmark()
        return result.map { reverseBookmarkListUseCase(it) }
    }
}