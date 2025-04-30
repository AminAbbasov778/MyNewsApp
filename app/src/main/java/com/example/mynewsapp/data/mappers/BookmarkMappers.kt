package com.example.mynewsapp.data.mappers

import com.example.mynewsapp.data.local.entity.BookmarkEntity
import com.example.mynewsapp.data.local.entity.SourceEntity
import com.example.mynewsapp.domain.domainmodels.ArticleModel


fun ArticleModel.toData(): BookmarkEntity{
    return BookmarkEntity(
        source = SourceEntity(source?.id ?: "0" ,source?.name ?: "Empty source") ,
        urlToImage = urlToImage ?: "No Image",
        title = title ?: "No Title",
        description = description ?: "No Description",
        timeDifference = timeDifference ?: "No Time Info",
        url = url ?: "Empty URL",
        author = author ?: "Empty author",
        content = content ?: "Empty content",
        publishedAt = publishedAt ?: "Empty published at"

    )
}