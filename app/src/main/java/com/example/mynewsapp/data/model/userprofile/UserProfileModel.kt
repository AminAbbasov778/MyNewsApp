package com.example.mynewsapp.data.model.userprofile

data class UserProfileModel(
    val fullName: String = "",
    val bio: String = "",
    val email: String = "",
    val imageBase64: String = "",
    val username: String = "",
    val phoneNumber: String = "",
    val website: String = "",
) {
}