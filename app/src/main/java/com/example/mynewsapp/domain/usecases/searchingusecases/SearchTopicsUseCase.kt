package com.example.mynewsapp.domain.usecases.searchingusecases

import com.example.mynewsapp.domain.domainmodels.TopicModel
import javax.inject.Inject

class SearchTopicsUseCase @Inject constructor() {
    operator fun invoke(topics: List<TopicModel>, query: String): List<TopicModel> {
        return topics.filter { topic -> topic.topic.contains(query, ignoreCase = true) == true}
    }
}