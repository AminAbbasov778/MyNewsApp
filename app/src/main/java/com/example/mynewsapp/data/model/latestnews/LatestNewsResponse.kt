package com.example.mynewsapp.data.model.latestnews


import com.google.gson.annotations.SerializedName

data class LatestNewsResponse(
    @SerializedName("articles")
    val articles: List<Article>,
    @SerializedName("status")
    val status: String,
    @SerializedName("totalResults")
    val totalResults: Int,
)