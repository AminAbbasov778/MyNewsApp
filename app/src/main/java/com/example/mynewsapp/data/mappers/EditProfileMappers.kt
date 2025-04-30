package com.example.mynewsapp.data.mappers

import com.example.mynewsapp.data.model.userprofile.Profile
import com.example.mynewsapp.domain.domainmodels.ProfileModel


fun ProfileModel.toData(): Profile{
    return Profile(fullName,bio,email,imageBase64,username,phoneNumber,website)
}