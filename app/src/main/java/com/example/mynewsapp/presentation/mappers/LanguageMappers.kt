package com.example.mynewsapp.presentation.mappers

import com.example.mynewsapp.domain.domainmodels.LanguageModel
import com.example.mynewsapp.presentation.uimodels.language.LanguageUiModel

fun LanguageModel.toUi(): LanguageUiModel{
    return LanguageUiModel(language,code,false)
}

