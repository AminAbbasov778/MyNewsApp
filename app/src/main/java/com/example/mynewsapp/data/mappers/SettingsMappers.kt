package com.example.mynewsapp.data.mappers

import com.example.mynewsapp.data.model.settings.Settings
import com.example.mynewsapp.domain.domainmodels.SettingsModel

fun Settings.toDomain(): SettingsModel{
    return SettingsModel(iconId,title)

}