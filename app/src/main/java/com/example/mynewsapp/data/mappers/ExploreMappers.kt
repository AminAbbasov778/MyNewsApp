package com.example.mynewsapp.data.mappers

import com.example.mynewsapp.data.model.topic.TopicEntity
import com.example.mynewsapp.domain.domainmodels.TopicModel

fun TopicEntity.toDomain(): TopicModel{
    return TopicModel(topicImg,topic,topicDescription)
}