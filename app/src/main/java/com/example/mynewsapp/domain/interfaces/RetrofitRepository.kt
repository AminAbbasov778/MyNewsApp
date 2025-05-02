package com.example.mynewsapp.domain.interfaces

import com.example.mynewsapp.data.model.latestnews.LatestNewsResponse
import com.example.mynewsapp.domain.domainmodels.ArticleModel

interface RetrofitRepository {

    suspend fun getLatestNews(
        keyword: String,
        sortBy: String,
        pageSize: Int?,
        page: Int?,
    ): Result<List<ArticleModel>>
}