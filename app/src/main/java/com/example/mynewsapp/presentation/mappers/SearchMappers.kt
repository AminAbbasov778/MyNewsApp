package com.example.mynewsapp.presentation.mappers

import com.example.mynewsapp.domain.domainmodels.AuthorModel
import com.example.mynewsapp.domain.domainmodels.TopicModel
import com.example.mynewsapp.presentation.uimodels.author.AuthorUiModel
import com.example.mynewsapp.presentation.uimodels.common.TopicUiModel

fun TopicUiModel.toDomain(): TopicModel{
    return TopicModel(topicImg,topic,topicDescription)
}

fun AuthorUiModel.toDomain(): AuthorModel{
    return AuthorModel(sourceName,sourceImg,sourceFollowerCount)
}

fun TopicModel.toUi(): TopicUiModel{
    return TopicUiModel(topicImg,topic,topicDescription,false)
}
fun AuthorModel.toUi(isFollowed : Boolean = false): AuthorUiModel{
    return AuthorUiModel(sourceName,sourceImg,sourceFollowerCount,isFollowed)
}