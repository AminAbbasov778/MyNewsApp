package com.example.mynewsapp.domain.usecases.exploreusecases

import com.example.mynewsapp.domain.interfaces.TopicRepository
import javax.inject.Inject

class SaveTopicUseCase @Inject constructor(val topicRepository: TopicRepository) {
    suspend operator fun invoke(topicName : String): Result<Unit> = topicRepository.saveTopic(topicName)
}