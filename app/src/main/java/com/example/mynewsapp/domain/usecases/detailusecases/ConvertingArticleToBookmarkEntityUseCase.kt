package com.example.mynewsapp.domain.usecases.detail

import com.example.mynewsapp.data.local.entity.BookmarkEntity
import com.example.mynewsapp.data.local.entity.SourceEntity
import com.example.mynewsapp.data.model.latestnews.Article
import javax.inject.Inject

class ConvertingArticleToBookmarkEntityUseCase @Inject constructor() {
    operator fun invoke(article: Article): BookmarkEntity {
        return BookmarkEntity(
            source = SourceEntity(article.source?.id ?: "0" ,article.source?.name ?: "Empty source") ,
            urlToImage = article.urlToImage ?: "No Image",
            title = article.title ?: "No Title",
            description = article.description ?: "No Description",
            timeDifference = article.timeDifference ?: "No Time Info",
            url = article.url ?: "Empty URL",
            author = article.author ?: "Empty author",
            content = article.content ?: "Empty content",
            publishedAt = article.publishedAt ?: "Empty published at"

        )
    }
}