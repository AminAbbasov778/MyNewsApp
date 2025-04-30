package com.example.mynewsapp.presentation.uimodels.common

import android.os.Parcelable
import com.example.mynewsapp.data.model.latestnews.Source
import kotlinx.android.parcel.Parcelize

@Parcelize
class ArticleUiModel(  val author: String?,
                       val content: String?,
                       val description: String?,
                       val publishedAt: String?,
                       val source: Source?,
                       val title: String?,
                       val url: String? = "No Url",
                       val urlToImage: String?,
                       val timeDifference: String?,): Parcelable {
}