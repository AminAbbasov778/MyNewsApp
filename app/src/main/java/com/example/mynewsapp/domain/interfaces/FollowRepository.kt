package com.example.mynewsapp.domain.interfaces

import com.example.mynewsapp.domain.domainmodels.FollowModel
import kotlinx.coroutines.flow.Flow

interface FollowRepository  {
    suspend fun followNewsSource(follow: FollowModel): Result<Unit>
     suspend fun unfollowNewsSource(sourceName: String): Result<Unit>
    suspend fun isNewsSourceFollowed(sourceName: String): Result<Boolean>
    suspend fun getFollowedSources(): Flow<Result<List<FollowModel>>>

}