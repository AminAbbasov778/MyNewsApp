package com.example.mynewsapp.data.repositories

import com.example.mynewsapp.R
import com.example.mynewsapp.domain.interfaces.SettingsRepository
import com.example.mynewsapp.presentation.uimodels.settings.SettingsModel
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor() : SettingsRepository {
    override fun getSettingsTypes(): ArrayList<SettingsModel> = arrayListOf(
        SettingsModel(R.drawable.notification_icon, R.string.notification,),
        SettingsModel(R.drawable.lock_icon, R.string.security),
        SettingsModel(R.drawable.help_icon, R.string.help),
        SettingsModel(R.drawable.language_icon, R.string.language),
        SettingsModel(R.drawable.display_icon, R.string.display),
        SettingsModel(R.drawable.logout_icon, R.string.logout),
    )


}