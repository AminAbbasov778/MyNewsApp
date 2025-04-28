package com.example.mynewsapp.domain.usecases.exploreusecases

import com.example.mynewsapp.domain.interfaces.TopicRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSavedTopicsUseCase @Inject constructor(val topicRepository: TopicRepository) {
    suspend operator fun invoke() : Flow<Result<List<String>>> = topicRepository.getSavedTopics()
}