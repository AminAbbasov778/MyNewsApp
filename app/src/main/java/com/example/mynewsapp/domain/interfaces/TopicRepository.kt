package com.example.mynewsapp.domain.interfaces

import com.example.mynewsapp.data.model.topic.TopicEntity
import com.example.mynewsapp.domain.domainmodels.TopicModel
import kotlinx.coroutines.flow.Flow

interface TopicRepository {
    fun getTopics(): List<TopicModel>
    suspend fun saveTopic(topicName : String): Result<Unit>
    suspend fun unSaveTopic(topicName : String): Result<Unit>
    suspend fun getSavedTopics(): Flow<Result<List<String>>>
}

