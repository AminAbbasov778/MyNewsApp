package com.example.mynewsapp.data.repositories

import com.example.mynewsapp.R
import com.example.mynewsapp.data.model.settings.Settings
import com.example.mynewsapp.domain.interfaces.SettingsRepository
import com.example.mynewsapp.presentation.uimodels.settings.SettingsUiModel
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor() : SettingsRepository {
    override fun getSettingsTypes(): ArrayList<Settings> = arrayListOf(
        Settings(R.drawable.notification_icon, R.string.notification,),
        Settings(R.drawable.lock_icon, R.string.security),
        Settings(R.drawable.help_icon, R.string.help),
        Settings(R.drawable.language_icon, R.string.language),
        Settings(R.drawable.display_icon, R.string.display),
        Settings(R.drawable.logout_icon, R.string.logout),
    )


}