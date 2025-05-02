package com.example.mynewsapp.data.mappers

import com.example.mynewsapp.data.local.entity.BookmarkEntity
import com.example.mynewsapp.data.local.entity.SourceEntity
import com.example.mynewsapp.data.model.latestnews.Source
import com.example.mynewsapp.domain.domainmodels.ArticleModel
import com.example.mynewsapp.domain.usecases.commonusecases.ChangeIsoToMillisFromApiUseCase
import com.example.mynewsapp.domain.usecases.commonusecases.TimeDifferenceUseCase


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

fun BookmarkEntity.toDomain(): ArticleModel{
    return  ArticleModel(
        author,
        content,
        description,
        publishedAt,
        Source(author, source.name),
        title,
        url,
        urlToImage
    )
}