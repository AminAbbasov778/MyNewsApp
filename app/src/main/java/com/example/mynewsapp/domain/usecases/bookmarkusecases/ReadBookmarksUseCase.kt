package com.example.mynewsapp.domain.usecases.bookmark

import com.example.mynewsapp.data.local.entity.BookmarkEntity
import com.example.mynewsapp.domain.interfaces.BookmarkDatabaseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReadBookmarksUseCase @Inject constructor(
    private val databaseRepository: BookmarkDatabaseRepository,
) {
    operator fun invoke(): Result<Flow<List<BookmarkEntity>>> {
        return databaseRepository.readBookmark()

    }
}