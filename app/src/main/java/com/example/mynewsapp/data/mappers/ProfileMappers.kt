package com.example.mynewsapp.data.mappers

import com.example.mynewsapp.data.model.usernews.UserNews
import com.example.mynewsapp.data.model.userprofile.Profile
import com.example.mynewsapp.domain.domainmodels.ProfileModel
import com.example.mynewsapp.domain.domainmodels.UserNewsModel

fun UserNews.toDomain(): UserNewsModel{
    return UserNewsModel(newsArticle,newsTitle,imageBase64,profileImageBase64,fullName,publishedAt)
}

fun Profile.toDomain(): ProfileModel{
    return ProfileModel(fullName,bio,email,imageBase64,username,phoneNumber,website)
}
fun UserNewsModel.toData(): UserNews{
    return UserNews(newsArticle,newsTitle,imageBase64,profileImageBase64,fullName,publishedAt)
}