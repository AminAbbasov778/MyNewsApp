package com.example.mynewsapp.domain.interfaces

import com.example.mynewsapp.data.model.settings.Settings
import com.example.mynewsapp.domain.domainmodels.SettingsModel
import com.example.mynewsapp.presentation.uimodels.settings.SettingsUiModel

interface SettingsRepository {
    fun getSettingsTypes(): List<SettingsModel>
}