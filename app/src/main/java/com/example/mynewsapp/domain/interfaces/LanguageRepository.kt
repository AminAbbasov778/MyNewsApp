package com.example.mynewsapp.domain.interfaces

import com.example.mynewsapp.data.model.language.LanguageModel

interface LanguageRepository {
    fun getCurrentLanguage(): Result<String>
    fun setLanguage(language: String): Result<Unit>
    fun getLanguageList(): List<LanguageModel>
}