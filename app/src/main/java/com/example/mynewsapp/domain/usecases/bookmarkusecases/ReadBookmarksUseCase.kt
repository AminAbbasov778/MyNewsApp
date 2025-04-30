package com.example.mynewsapp.domain.usecases.bookmark


import com.example.mynewsapp.domain.domainmodels.ArticleModel
import com.example.mynewsapp.domain.interfaces.BookmarkDatabaseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReadBookmarksUseCase @Inject constructor(
    private val databaseRepository: BookmarkDatabaseRepository,
    private val reverseBookmarkListUseCase: ReverseBookmarkListUseCase,
    private val convertBookmarkEntityToArticleUseCase: ConvertBookmarkEntityToArticleUseCase,

) {
    operator fun invoke(): Result<Flow<List<ArticleModel>>> {
        val result = databaseRepository.readBookmark()
      return result.map {bookmarksFlow -> reverseBookmarkListUseCase(convertBookmarkEntityToArticleUseCase(bookmarksFlow)) }


    }
}