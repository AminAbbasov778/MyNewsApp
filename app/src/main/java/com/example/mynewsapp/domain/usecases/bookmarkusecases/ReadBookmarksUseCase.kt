package com.example.mynewsapp.domain.usecases.bookmark


import com.example.mynewsapp.domain.domainmodels.ArticleModel
import com.example.mynewsapp.domain.interfaces.BookmarkDatabaseRepository
import com.example.mynewsapp.domain.usecases.commonusecases.ChangeIsoToMillisFromApiUseCase
import com.example.mynewsapp.domain.usecases.commonusecases.TimeDifferenceUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ReadBookmarksUseCase @Inject constructor(
    private val databaseRepository: BookmarkDatabaseRepository,
    private val reverseBookmarkListUseCase: ReverseBookmarkListUseCase,
val timeDifferenceUseCase: TimeDifferenceUseCase,
    val isoToMillisFromApiUseCase: ChangeIsoToMillisFromApiUseCase
) {
    operator fun invoke(): Result<Flow<List<ArticleModel>>> {
        val result = databaseRepository.readBookmark()
      val updatedResult =   result.map {bookmarksFlow -> bookmarksFlow.map { it.map { it.copy(timeDifference =timeDifferenceUseCase(isoToMillisFromApiUseCase(it.publishedAt)) ) } } }
      return updatedResult.map {bookmarksFlow -> reverseBookmarkListUseCase(bookmarksFlow) }

    }
}