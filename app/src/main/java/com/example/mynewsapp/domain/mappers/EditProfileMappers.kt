package com.example.mynewsapp.domain.mappers

import com.example.mynewsapp.domain.domainmodels.ProfileModel
import com.example.mynewsapp.presentation.uimodels.profile.NewProfileUiModel

fun NewProfileUiModel.toDomain(): ProfileModel{
    return ProfileModel(fullName,bio,email,imageBase64,username,phoneNumber,website)
}