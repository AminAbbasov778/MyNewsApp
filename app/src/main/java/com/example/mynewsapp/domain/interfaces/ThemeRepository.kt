package com.example.mynewsapp.domain.interfaces

interface ThemeRepository {
    fun saveTheme(isDarkMode: Boolean): Result<Unit>
    fun getTheme(): Result<Boolean>
}