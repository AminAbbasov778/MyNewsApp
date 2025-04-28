package com.example.mynewsapp.domain.usecases.detailusecases

import com.example.mynewsapp.domain.interfaces.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsNewsFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    suspend operator fun invoke(url: String): Flow<Result<Boolean>> {
        return favoriteRepository.isNewsFavorite(url)
    }
}