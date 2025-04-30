package com.example.mynewsapp.presentation.mappers

import com.example.mynewsapp.domain.domainmodels.TopicModel
import com.example.mynewsapp.presentation.uimodels.common.TopicUiModel

fun TopicModel.toUi(isSaved : Boolean): TopicUiModel{
    return TopicUiModel(topicImg,topic,topicDescription,isSaved)
}