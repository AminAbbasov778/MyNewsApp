package com.example.mynewsapp.domain.usecases.detail

import com.example.mynewsapp.domain.interfaces.BookmarkDatabaseRepository
import javax.inject.Inject

class DeleteBookmarkUseCase @Inject constructor(
    private val databaseRepository: BookmarkDatabaseRepository,
) {
    suspend operator fun invoke(articleUrl: String): Result<Boolean> =
        databaseRepository.deleteBookmark(articleUrl)

}