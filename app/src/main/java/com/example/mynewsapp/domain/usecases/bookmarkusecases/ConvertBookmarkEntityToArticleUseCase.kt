package com.example.mynewsapp.domain.usecases.bookmark

import com.example.mynewsapp.data.local.entity.BookmarkEntity
import com.example.mynewsapp.data.model.latestnews.Article
import com.example.mynewsapp.data.model.latestnews.Source

class ConvertBookmarkEntityToArticleUseCase {
    operator fun invoke(bookmark: BookmarkEntity): Article {
        return Article(
            author = "null",
            content = "null",
            description = bookmark.newDescription,
            publishedAt = "null",
            source = Source(id = "null", name = bookmark.sourceName),
            title = bookmark.newsTitle,
            url = bookmark.url,
            urlToImage = bookmark.imageUrl,
            timeDifference = bookmark.timeDifference
        )
    }
}