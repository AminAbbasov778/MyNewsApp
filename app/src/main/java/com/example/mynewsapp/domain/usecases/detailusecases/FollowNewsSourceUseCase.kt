package com.example.mynewsapp.domain.usecases.detailusecases

import com.example.mynewsapp.domain.domainmodels.FollowModel
import com.example.mynewsapp.domain.interfaces.FollowRepository
import javax.inject.Inject

class FollowNewsSourceUseCase @Inject constructor(val followRepository: FollowRepository) {
    suspend operator fun invoke(follow: FollowModel): Result<Unit>  = followRepository.followNewsSource(follow)

}