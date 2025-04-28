package com.example.mynewsapp.domain.usecases.detailusecases

import com.example.mynewsapp.domain.interfaces.FavoriteRepository
import javax.inject.Inject

class FavoriteNewsUseCase @Inject constructor(val favoriteRepository: FavoriteRepository) {
    suspend operator fun invoke(url : String) : Result<Unit> = favoriteRepository.favoriteNews(url)

}