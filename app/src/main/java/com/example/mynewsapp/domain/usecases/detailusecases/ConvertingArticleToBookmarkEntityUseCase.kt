package com.example.mynewsapp.domain.usecases.detail

import com.example.mynewsapp.data.local.entity.BookmarkEntity
import com.example.mynewsapp.data.model.latestnews.Article
import javax.inject.Inject

class ConvertingArticleToBookmarkEntityUseCase @Inject constructor() {
    operator fun invoke(article: Article): BookmarkEntity {
        return BookmarkEntity(
            sourceName = article.source?.name ?: "Unknown Source",
            imageUrl = article.urlToImage ?: "No Image",
            newsTitle = article.title ?: "No Title",
            newDescription = article.description ?: "No Description",
            timeDifference = article.timeDifference ?: "No Time Info",
            url = article.url ?: "Empty URL"
        )
    }
}