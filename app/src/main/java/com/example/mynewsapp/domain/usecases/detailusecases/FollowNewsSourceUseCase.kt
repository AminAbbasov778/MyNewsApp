package com.example.mynewsapp.domain.usecases.detailusecases

import com.example.mynewsapp.data.model.follow.FollowModel
import com.example.mynewsapp.domain.interfaces.FollowRepository
import javax.inject.Inject

class FollowNewsSourceUseCase @Inject constructor(val followRepository: FollowRepository) {
    suspend operator fun invoke(followModel: FollowModel): Result<Unit>  = followRepository.followNewsSource(followModel)

}