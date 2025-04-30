package com.example.mynewsapp.domain.usecases.settingsusecases

import com.example.mynewsapp.domain.interfaces.SettingsRepository
import com.example.mynewsapp.presentation.uimodels.settings.SettingsModel
import javax.inject.Inject

class GetSettingsTypesUseCases @Inject constructor(val settingsRepository: SettingsRepository)  {
    operator fun invoke(): ArrayList<SettingsModel> = settingsRepository.getSettingsTypes()
}