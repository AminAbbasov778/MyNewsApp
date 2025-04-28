package com.example.mynewsapp.domain.usecases.detailusecases

import com.example.mynewsapp.domain.interfaces.FollowRepository
import javax.inject.Inject

class UnfollowNewSourceUseCase @Inject constructor(val followRepository: FollowRepository) {
    suspend operator fun invoke(sourceName : String): Result<Unit> = followRepository.unfollowNewsSource(sourceName)
}