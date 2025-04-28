package com.example.mynewsapp.domain.usecases.profileUseCase

import com.example.mynewsapp.domain.interfaces.UserRepository
import javax.inject.Inject

class DeleteNewsByPublishedAtUseCase @Inject constructor(val userRepository: UserRepository) {
    suspend operator fun invoke(publishedAt: String) : Result<Unit> = userRepository.deleteNewsByPublishedAt(publishedAt)
}