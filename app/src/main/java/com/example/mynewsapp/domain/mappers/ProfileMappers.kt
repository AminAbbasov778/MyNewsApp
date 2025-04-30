package com.example.mynewsapp.domain.mappers

import com.example.mynewsapp.data.model.usernews.UserNews
import com.example.mynewsapp.domain.domainmodels.UserNewsModel

fun UserNews.toDomain(): UserNewsModel{
    return UserNewsModel(newsArticle,newsTitle,imageBase64,profileImageBase64,fullName,publishedAt)
}