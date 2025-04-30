package com.example.mynewsapp.domain.usecases.commonusecases

import com.example.mynewsapp.data.model.follow.Follow
import com.example.mynewsapp.domain.domainmodels.FollowModel
import com.example.mynewsapp.domain.interfaces.FollowRepository
import com.example.mynewsapp.domain.mappers.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetFollowedSourcesUseCase @Inject constructor(val followRepository: FollowRepository) {
    suspend operator fun invoke(): Flow<Result<List<FollowModel>>>  {
       return followRepository.getFollowedSources().map { result ->
            result.map { list -> list.map { it.toDomain() } }
        }
    }
}