package com.example.mynewsapp.domain.usecases.displayusecases

import com.example.mynewsapp.domain.interfaces.ThemeRepository
import javax.inject.Inject

class GetThemeUseCase @Inject constructor(val themeRepository: ThemeRepository) {
    operator fun invoke(): Result<Boolean> = themeRepository.getTheme()

}