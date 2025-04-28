package com.example.mynewsapp.domain.interfaces

import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    suspend fun favoriteNews(url: String): Result<Unit>
    suspend fun unFavoriteNews(url: String): Result<Unit>
    suspend fun isNewsFavorite(url: String): Flow<Result<Boolean>>
    suspend fun getFavoriteCount(url: String): Flow<Result<Int>>
}