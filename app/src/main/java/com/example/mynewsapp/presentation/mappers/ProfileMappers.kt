package com.example.mynewsapp.presentation.mappers

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import com.example.mynewsapp.domain.domainmodels.UserNewsModel
import com.example.mynewsapp.presentation.uimodels.createnews.UserNewsUiModel

fun UserNewsModel.toUi(imageBitmap: Bitmap?,profileImageBitmap: Bitmap?,timeDifference: String): UserNewsUiModel{
    return UserNewsUiModel(imageBitmap,newsTitle,newsArticle,profileImageBitmap,fullName,timeDifference,publishedAt)
}