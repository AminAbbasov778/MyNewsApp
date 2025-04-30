package com.example.mynewsapp.domain.interfaces

import com.example.mynewsapp.data.model.language.Language

interface LanguageRepository {
    fun getCurrentLanguage(): Result<String>
    fun setLanguage(language: String): Result<Unit>
    fun getLanguageList(): List<Language>
}