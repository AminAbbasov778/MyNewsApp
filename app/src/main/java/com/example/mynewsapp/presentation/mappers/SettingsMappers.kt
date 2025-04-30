package com.example.mynewsapp.presentation.mappers

import com.example.mynewsapp.domain.domainmodels.SettingsModel
import com.example.mynewsapp.presentation.uimodels.settings.SettingsUiModel

fun SettingsModel.toUi(): SettingsUiModel{
    return SettingsUiModel(iconId,title)
}