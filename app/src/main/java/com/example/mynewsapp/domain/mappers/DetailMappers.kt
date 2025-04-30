package com.example.mynewsapp.domain.mappers

import com.example.mynewsapp.data.model.follow.Follow
import com.example.mynewsapp.domain.domainmodels.ArticleModel
import com.example.mynewsapp.domain.domainmodels.FollowModel
import com.example.mynewsapp.presentation.uimodels.common.ArticleUiModel
import com.example.mynewsapp.presentation.uimodels.common.FollowUiModel

fun ArticleUiModel.toDomain(): ArticleModel {
    return ArticleModel(
        author,
        content,
        description,
        publishedAt,
        source,
        title,
        url,
        urlToImage,
        timeDifference
    )
}

fun FollowUiModel.toDomain(): FollowModel{
    return FollowModel(sourceName,sourceImg,sourceFollowerCount)
}

fun Follow.toDomain(): FollowModel{
    return FollowModel(sourceName,sourceImg,sourceFollowerCount)
}