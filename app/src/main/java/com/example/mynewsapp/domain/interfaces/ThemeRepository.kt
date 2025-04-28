package com.example.mynewsapp.domain.interfaces

import kotlinx.coroutines.flow.Flow

interface ThemeRepository {
    fun saveTheme(isDarkMode: Boolean): Result<Unit>
    fun getTheme(): Result<Boolean>
}