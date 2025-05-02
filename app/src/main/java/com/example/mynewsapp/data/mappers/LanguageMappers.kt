package com.example.mynewsapp.data.mappers

import com.example.mynewsapp.data.model.language.Language
import com.example.mynewsapp.domain.domainmodels.LanguageModel

fun Language.toDomain(): LanguageModel{
    return LanguageModel(language,code)
}