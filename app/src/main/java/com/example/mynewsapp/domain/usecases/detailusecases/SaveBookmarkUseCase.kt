package com.example.mynewsapp.domain.usecases.detail

import com.example.mynewsapp.data.model.latestnews.Article
import com.example.mynewsapp.domain.interfaces.BookmarkDatabaseRepository
import javax.inject.Inject

class SaveBookmarkUseCase @Inject constructor(
    private val databaseRepository: BookmarkDatabaseRepository,
    private val convertingArticleToBookmarkEntityUseCase: ConvertingArticleToBookmarkEntityUseCase,
) {
    suspend operator fun invoke(article: Article): Result<Boolean> {

        val bookmarkEntity = convertingArticleToBookmarkEntityUseCase(article)
        return databaseRepository.writeBookmark(bookmarkEntity)

    }
}