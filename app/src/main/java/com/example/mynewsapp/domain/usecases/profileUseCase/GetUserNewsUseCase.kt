package com.example.mynewsapp.domain.usecases.profileUseCase

import com.example.mynewsapp.data.model.usernews.UserNews
import com.example.mynewsapp.domain.domainmodels.UserNewsModel
import com.example.mynewsapp.domain.interfaces.UserRepository
import com.example.mynewsapp.domain.mappers.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetUserNewsUseCase @Inject constructor(val userRepository: UserRepository) {
    suspend operator fun invoke(): Flow<Result<List<UserNewsModel>>> = userRepository.getUserNews()
}