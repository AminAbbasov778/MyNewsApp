package com.example.mynewsapp.domain.usecases.detailusecases

import com.example.mynewsapp.domain.interfaces.FavoriteRepository
import javax.inject.Inject

class UnFavoriteNewUseCase @Inject constructor(val favoriteRepository: FavoriteRepository) {
    suspend operator fun invoke(url : String): Result<Unit> = favoriteRepository.unFavoriteNews(url)
}