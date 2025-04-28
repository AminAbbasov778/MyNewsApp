package com.example.mynewsapp.domain.usecases.detailusecases

import com.example.mynewsapp.domain.interfaces.FollowRepository
import javax.inject.Inject

class IsNewsSourceFollowedUseCase @Inject constructor(val followRepository: FollowRepository) {
    suspend operator fun invoke(sourceName : String): Result<Boolean> = followRepository.isNewsSourceFollowed(sourceName)
}