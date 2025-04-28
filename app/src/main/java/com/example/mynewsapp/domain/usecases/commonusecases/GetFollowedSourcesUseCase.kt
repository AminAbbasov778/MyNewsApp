package com.example.mynewsapp.domain.usecases.commonusecases

import com.example.mynewsapp.data.model.follow.FollowModel
import com.example.mynewsapp.domain.interfaces.FollowRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFollowedSourcesUseCase @Inject constructor(val followRepository: FollowRepository) {
    suspend operator fun invoke(): Flow<Result<List<FollowModel>>> = followRepository.getFollowedSources()
}