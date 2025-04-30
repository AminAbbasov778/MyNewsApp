package com.example.mynewsapp.domain.interfaces

import com.example.mynewsapp.presentation.uimodels.settings.SettingsModel

interface SettingsRepository {
    fun getSettingsTypes():ArrayList<SettingsModel>
}