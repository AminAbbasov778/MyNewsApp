package com.example.mynewsapp.domain.interfaces

import com.example.mynewsapp.domain.domainmodels.SettingsModel

interface SettingsRepository {
    fun getSettingsTypes(): List<SettingsModel>
}