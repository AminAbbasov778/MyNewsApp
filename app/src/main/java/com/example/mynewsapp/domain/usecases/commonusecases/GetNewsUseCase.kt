package com.example.mynewsapp.domain.usecases.commonusecases

import com.example.mynewsapp.data.model.latestnews.Article
import com.example.mynewsapp.data.model.latestnews.LatestNewsResponse
import com.example.mynewsapp.domain.domainmodels.ArticleModel
import com.example.mynewsapp.domain.interfaces.RetrofitRepository
import com.example.mynewsapp.domain.mappers.toDomain
import javax.inject.Inject

class GetNewsUseCase @Inject constructor(
    private val retrofitRepository: RetrofitRepository,
) {
    suspend operator fun invoke(
        keyWord: String,
        sortBy: String,
        pageSize: Int?,
        page: Int? = null,
    ): Result<List<ArticleModel>> {

        val result = retrofitRepository.getLatestNews(keyWord, sortBy, pageSize, page)
        return  result.map {result -> result.articles.map {news -> news.toDomain() }  }
    }
}