package com.example.mynewsapp.domain.interfaces

import com.example.mynewsapp.data.model.follow.FollowModel
import kotlinx.coroutines.flow.Flow

interface FollowRepository  {
    suspend fun followNewsSource(followModel: FollowModel): Result<Unit>
     suspend fun unfollowNewsSource(sourceName: String): Result<Unit>
    suspend fun isNewsSourceFollowed(sourceName: String): Result<Boolean>
    suspend fun getFollowedSources(): Flow<Result<List<FollowModel>>>

}