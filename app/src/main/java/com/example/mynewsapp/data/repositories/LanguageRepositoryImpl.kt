package com.example.mynewsapp.data.repositories

import android.content.SharedPreferences
import com.example.mynewsapp.data.model.language.LanguageModel
import com.example.mynewsapp.domain.interfaces.LanguageRepository
import javax.inject.Inject
import javax.inject.Named

class LanguageRepositoryImpl @Inject constructor(
    @Named("default") val sharedPreferences: SharedPreferences,
) : LanguageRepository {

    override fun getCurrentLanguage(): Result<String> {
        return try {
            val language = sharedPreferences.getString("language", "en") ?: "en"
            Result.success(language)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun setLanguage(language: String): Result<Unit> {
        return try {
            sharedPreferences.edit().putString("language", language).apply()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    override fun getLanguageList(): List<LanguageModel> =
        listOf<LanguageModel>(LanguageModel("English", "en"), LanguageModel("Azerbaijani", "az"))
}