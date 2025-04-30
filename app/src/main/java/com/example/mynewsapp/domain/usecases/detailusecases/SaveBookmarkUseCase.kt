package com.example.mynewsapp.domain.usecases.detail

import com.example.mynewsapp.domain.domainmodels.ArticleModel
import com.example.mynewsapp.domain.interfaces.BookmarkDatabaseRepository
import com.example.mynewsapp.domain.mappers.toDomain
import com.example.mynewsapp.presentation.uimodels.common.ArticleUiModel
import javax.inject.Inject

class SaveBookmarkUseCase @Inject constructor(
    private val databaseRepository: BookmarkDatabaseRepository,
) {
    suspend operator fun invoke(article: ArticleUiModel): Result<Boolean> {
        return databaseRepository.writeBookmark(article.toDomain())

    }
}