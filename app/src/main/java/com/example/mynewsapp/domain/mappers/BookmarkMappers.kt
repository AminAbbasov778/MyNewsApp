package com.example.mynewsapp.domain.mappers

import com.example.mynewsapp.data.local.entity.BookmarkEntity
import com.example.mynewsapp.data.model.latestnews.Source
import com.example.mynewsapp.domain.domainmodels.ArticleModel
import com.example.mynewsapp.domain.usecases.commonusecases.ChangeIsoToMillisFromApiUseCase
import com.example.mynewsapp.domain.usecases.commonusecases.TimeDifferenceUseCase

fun BookmarkEntity.toDomain(timeDifferenceUseCase: TimeDifferenceUseCase,isoToMillisFromApiUseCase: ChangeIsoToMillisFromApiUseCase): ArticleModel{
  return  ArticleModel(
         author,
        content,
        description,
        publishedAt,
        Source(author, source.name),
         title,
        url,
        urlToImage,
         timeDifferenceUseCase(isoToMillisFromApiUseCase(publishedAt))
    )
}