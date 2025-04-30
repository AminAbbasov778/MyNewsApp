package com.example.mynewsapp.domain.usecases.createnewsusecases

import com.example.mynewsapp.data.model.usernews.UserNews
import com.example.mynewsapp.domain.interfaces.UserRepository
import javax.inject.Inject

class CreateNewsUseCase @Inject constructor(val userRepository: UserRepository) {
    suspend operator fun invoke(userNews: UserNews) : Result<Unit> = userRepository.createNews(userNews)
}