package com.example.mynewsapp.domain.usecases.bookmark

import com.example.mynewsapp.data.local.entity.BookmarkEntity
import com.example.mynewsapp.data.model.latestnews.Article
import com.example.mynewsapp.data.model.latestnews.Source
import com.example.mynewsapp.domain.usecases.commonusecases.ChangeIsoToMillisFromApiUseCase
import com.example.mynewsapp.domain.usecases.commonusecases.TimeDifferenceUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ConvertBookmarkEntityToArticleUseCase @Inject constructor(val timeDifferenceUseCase: TimeDifferenceUseCase,val isoToMillisFromApiUseCase: ChangeIsoToMillisFromApiUseCase) {
    operator fun invoke(bookmarkFlow : Flow<List<BookmarkEntity>>): Flow<List<Article>> {
        return bookmarkFlow.map {bookmarkList->
            bookmarkList.map {bookmark ->
                Article(
                    author = bookmark.author,
                    content = bookmark.content,
                    description = bookmark.description,
                    publishedAt = bookmark.publishedAt,
                    source = Source(id = bookmark.author, name = bookmark.source.name),
                    title = bookmark.title,
                    url = bookmark.url,
                    urlToImage = bookmark.urlToImage,
                    timeDifference = timeDifferenceUseCase(isoToMillisFromApiUseCase(bookmark.publishedAt))
                )
            }

        }
    }
}