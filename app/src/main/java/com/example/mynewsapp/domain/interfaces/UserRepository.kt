package com.example.mynewsapp.domain.interfaces

import com.example.mynewsapp.data.model.usernews.UserNews
import com.example.mynewsapp.data.model.userprofile.Profile
import com.example.mynewsapp.domain.domainmodels.ProfileModel
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun uploadProfileData(userProfile: ProfileModel): Result<Unit>
    suspend fun getProfileData(): Flow<Result<Profile>>
    suspend fun createNews(userNews: UserNews): Result<Unit>
    suspend fun getUserNews(): Flow<Result<List<UserNews>>>
    suspend fun deleteNewsByPublishedAt(publishedAt : String): Result<Unit>
    fun getUserId() : String?

}

