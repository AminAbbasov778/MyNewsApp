package com.example.mynewsapp.domain.interfaces

import com.example.mynewsapp.data.model.usernews.UserNewsModel
import com.example.mynewsapp.data.model.userprofile.UserProfileModel
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun uploadProfileData(userProfileModel: UserProfileModel): Result<Unit>
    suspend fun getProfileData(): Flow<Result<UserProfileModel>>
    suspend fun createNews(userNewsModel: UserNewsModel): Result<Unit>
    suspend fun getUserNews(): Flow<Result<List<UserNewsModel>>>
    suspend fun deleteNewsByPublishedAt(publishedAt : String): Result<Unit>
    fun getUserId() : String?

}

