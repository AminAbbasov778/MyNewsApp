package com.example.mynewsapp.domain.usecases.detailusecases

import com.example.mynewsapp.domain.interfaces.FavoriteRepository
import javax.inject.Inject

class UpdateFavoriteCountUseCase @Inject constructor(val favoriteRepository: FavoriteRepository) {
}