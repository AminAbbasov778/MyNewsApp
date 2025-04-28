package com.example.mynewsapp.presentation.uimodels.createnews

import android.graphics.Bitmap

data class UserNewsUiModel(
    val imageBitmap: Bitmap?,
    val newsTitle: String,
    val newsArticle: String,
    val profileImageBitmap: Bitmap?,
    val fullName: String,
    val timeDifference: String,
    val publishedAt : String
) {
}