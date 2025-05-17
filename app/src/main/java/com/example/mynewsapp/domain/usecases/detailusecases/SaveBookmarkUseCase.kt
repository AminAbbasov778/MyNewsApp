package com.example.mynewsapp.domain.usecases.detail

import com.example.mynewsapp.domain.domainmodels.ArticleModel
import com.example.mynewsapp.domain.interfaces.BookmarkDatabaseRepository
import javax.inject.Inject

class SaveBookmarkUseCase @Inject constructor(
    private val databaseRepository: BookmarkDatabaseRepository,
) {
    suspend operator fun invoke(article: ArticleModel): Result<Boolean> {
        return databaseRepository.writeBookmark(article)

    }
}