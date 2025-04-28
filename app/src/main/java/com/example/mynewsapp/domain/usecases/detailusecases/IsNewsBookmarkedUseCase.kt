package com.example.mynewsapp.domain.usecases.detail

import com.example.mynewsapp.domain.interfaces.BookmarkDatabaseRepository
import javax.inject.Inject

class IsNewsBookmarkedUseCase @Inject constructor(
    private val databaseRepository: BookmarkDatabaseRepository,
) {
    suspend operator fun invoke(url: String): Result<Boolean> =
        databaseRepository.isNewsBookmarked(url)


}