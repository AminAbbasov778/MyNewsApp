package com.example.mynewsapp.domain.usecases.settingsusecases

import com.example.mynewsapp.domain.domainmodels.SettingsModel
import com.example.mynewsapp.domain.interfaces.SettingsRepository
import com.example.mynewsapp.domain.mappers.toDomain
import com.example.mynewsapp.presentation.uimodels.settings.SettingsUiModel
import javax.inject.Inject

class GetSettingsTypesUseCases @Inject constructor(val settingsRepository: SettingsRepository)  {
    operator fun invoke(): List<SettingsModel> = settingsRepository.getSettingsTypes().map { it.toDomain() }
}