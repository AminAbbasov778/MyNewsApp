package com.example.mynewsapp.presentation.mappers

import com.example.mynewsapp.data.model.latestnews.Source
import com.example.mynewsapp.domain.domainmodels.ArticleModel
import com.example.mynewsapp.presentation.uimodels.common.ArticleUiModel

fun ArticleModel.toUi(): ArticleUiModel{
    return ArticleUiModel(
        urlToImage = urlToImage ?: "No Image Url",
        timeDifference = timeDifference,
        title = title ?: "No title",
        description = description ?: "No description",
        author = author ?: "No author",
        content = content ?: "No content",
        source = Source(source?.id ?: "No id", source?.name ?: "No name"),
        url = url ?: "No url",
        publishedAt = publishedAt ?: "No published at"
    )
}