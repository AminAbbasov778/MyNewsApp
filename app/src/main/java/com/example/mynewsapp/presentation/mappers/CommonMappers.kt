package com.example.mynewsapp.presentation.mappers

import com.example.mynewsapp.domain.domainmodels.FollowModel
import com.example.mynewsapp.presentation.uimodels.common.FollowUiModel

fun FollowModel.toUi(): FollowUiModel{
    return FollowUiModel(sourceName,sourceImg,sourceFollowerCount)
}