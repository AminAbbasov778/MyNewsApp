package com.example.mynewsapp.domain.usecases.detailusecases

import com.example.mynewsapp.data.model.follow.Follow
import com.example.mynewsapp.domain.interfaces.FollowRepository
import com.example.mynewsapp.domain.mappers.toDomain
import com.example.mynewsapp.presentation.uimodels.common.FollowUiModel
import javax.inject.Inject

class FollowNewsSourceUseCase @Inject constructor(val followRepository: FollowRepository) {
    suspend operator fun invoke(follow: FollowUiModel): Result<Unit>  = followRepository.followNewsSource(follow.toDomain())

}