package com.example.mynewsapp.domain.usecases.displayusecases

import com.example.mynewsapp.domain.interfaces.ThemeRepository
import javax.inject.Inject

class SaveThemeUseCase @Inject constructor(val themeRepository: ThemeRepository) {
     operator fun invoke(isDarkMode: Boolean): Result<Unit> = themeRepository.saveTheme(isDarkMode)


}