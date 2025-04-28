package com.example.mynewsapp.domain.usecases.profileUseCase

import com.example.mynewsapp.data.model.usernews.UserNewsModel
import com.example.mynewsapp.domain.interfaces.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserNewsUseCase @Inject constructor(val userRepository: UserRepository) {
    suspend operator fun invoke(): Flow<Result<List<UserNewsModel>>> = userRepository.getUserNews()
}