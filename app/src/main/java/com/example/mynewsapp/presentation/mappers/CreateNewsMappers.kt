package com.example.mynewsapp.presentation.mappers

import com.example.mynewsapp.domain.domainmodels.UserNewsModel
import com.example.mynewsapp.presentation.uimodels.createnews.NewUserNewsUiModel

fun NewUserNewsUiModel.toDomain(): UserNewsModel{
    return UserNewsModel(newsArticle,newsTitle,imageBase64,profileImageBase64,fullName,publishedAt)
}