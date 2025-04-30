package com.example.mynewsapp.domain.mappers

import com.example.mynewsapp.data.model.topic.TopicEntity
import com.example.mynewsapp.domain.domainmodels.TopicModel

fun TopicEntity.toDomain(): TopicModel{
    return TopicModel(topicImg,topic,topicDescription)
}