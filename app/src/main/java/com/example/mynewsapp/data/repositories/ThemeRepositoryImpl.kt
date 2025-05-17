package com.example.mynewsapp.data.repositories

import android.content.SharedPreferences
import com.example.mynewsapp.Utils.Constants
import com.example.mynewsapp.domain.interfaces.ThemeRepository
import javax.inject.Inject
import javax.inject.Named

class ThemeRepositoryImpl @Inject constructor(@Named("default") val sharedPreferences: SharedPreferences):
    ThemeRepository {
    override  fun saveTheme(isDarkMode: Boolean): Result<Unit> {
        return try {
            sharedPreferences.edit().putBoolean(Constants.KEY_DARK_MODE, isDarkMode).apply()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

        override  fun getTheme(): Result<Boolean> {
           return try {
                val isDarkMode = sharedPreferences.getBoolean(Constants.KEY_DARK_MODE, false)
                Result.success(isDarkMode)
            } catch (e: Exception) {
              Result.failure(e)
            }
        }

}