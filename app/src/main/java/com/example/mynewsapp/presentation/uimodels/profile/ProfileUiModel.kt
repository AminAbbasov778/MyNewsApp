package com.example.mynewsapp.presentation.uimodels.profile

import android.graphics.Bitmap

 class ProfileUiModel(
    val imageBitmap: Bitmap?,
    val fullName: String,
    val bio: String,
    val email: String,
    val username: String,
    val phoneNumber: String,
    val website: String,
) {
}