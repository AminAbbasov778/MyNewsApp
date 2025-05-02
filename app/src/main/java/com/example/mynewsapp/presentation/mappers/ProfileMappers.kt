package com.example.mynewsapp.presentation.mappers

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import com.example.mynewsapp.domain.domainmodels.ProfileModel
import com.example.mynewsapp.domain.domainmodels.UserNewsModel
import com.example.mynewsapp.presentation.uimodels.createnews.UserNewsUiModel
import com.example.mynewsapp.presentation.uimodels.profile.NewProfileUiModel
import com.example.mynewsapp.presentation.uimodels.profile.ProfileUiModel
import com.example.mynewsapp.presentation.uiutils.ImageUtils

fun UserNewsModel.toUi(timeDifference: String): UserNewsUiModel{
    return UserNewsUiModel(ImageUtils.base64ToBitmap(imageBase64),newsTitle,newsArticle, ImageUtils.base64ToBitmap(profileImageBase64),fullName,timeDifference,publishedAt)
}

fun ProfileModel.toUi(): NewProfileUiModel{
    return NewProfileUiModel(imageBase64,fullName,bio,email,username,phoneNumber,website)
}

fun ProfileModel.toProfileUiModel(): ProfileUiModel{
    return ProfileUiModel(ImageUtils.base64ToBitmap(imageBase64),fullName,bio,email,username,phoneNumber,website)
}
