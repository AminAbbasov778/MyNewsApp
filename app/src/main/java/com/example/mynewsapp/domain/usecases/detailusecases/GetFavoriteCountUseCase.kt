package com.example.mynewsapp.domain.usecases.detailusecases

import com.example.mynewsapp.domain.interfaces.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteCountUseCase @Inject constructor(val favoriteRepository: FavoriteRepository) {
    suspend operator fun invoke(url : String): Flow<Result<Int>> = favoriteRepository.getFavoriteCount(url)
}