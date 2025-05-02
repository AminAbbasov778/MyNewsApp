package com.example.mynewsapp.domain.domainmodels

import com.example.mynewsapp.data.model.latestnews.Source
import com.google.gson.annotations.SerializedName

data class ArticleModel(
                    val author: String?,
                    val content: String?,
                    val description: String?,
                    val publishedAt: String?,
                    val source: Source?,
                    val title: String?,
                    val url: String? = "No Url",
                    val urlToImage: String?,
                    val timeDifference: String? = "Empty",){
}