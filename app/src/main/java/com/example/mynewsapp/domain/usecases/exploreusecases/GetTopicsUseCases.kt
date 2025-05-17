package com.example.mynewsapp.domain.usecases.exploreusecases

import com.example.mynewsapp.domain.domainmodels.TopicModel
import com.example.mynewsapp.domain.interfaces.TopicRepository
import javax.inject.Inject

class GetTopicsUseCases @Inject constructor(val topicRepository: TopicRepository) {
    operator fun invoke() : List<TopicModel> = topicRepository.getTopics()
}