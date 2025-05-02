package com.example.mynewsapp.data.mappers

import com.example.mynewsapp.data.model.latestnews.Article
import com.example.mynewsapp.data.model.latestnews.Source
import com.example.mynewsapp.domain.domainmodels.ArticleModel

fun Article.toDomain(): ArticleModel{
    return  ArticleModel(
        urlToImage = urlToImage ?: "No Image Url",
        timeDifference = timeDifference ?: "Empty time difference",
        title = title ?: "No title",
        description = description ?: "No description",
        author = author ?: "No author",
        content = content ?: "No content",
        source = Source(source?.id ?: "No id",source?.name ?: "No name"),
        url = url ?: "No url",
        publishedAt = publishedAt ?: "No published at"
    )
}