package com.example.mynewsapp.presentation.mappers

import com.example.mynewsapp.domain.domainmodels.LanguageModel
import com.example.mynewsapp.presentation.uimodels.language.LanguageUiModel

fun LanguageModel.toUi(isSelected : Boolean): LanguageUiModel{
    return LanguageUiModel(language,code,isSelected)
}

