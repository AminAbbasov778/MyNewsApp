package com.example.mynewsapp.data.repositories

import com.example.mynewsapp.data.mappers.toDomain
import com.example.mynewsapp.data.remote.RequestService
import com.example.mynewsapp.domain.domainmodels.ArticleModel
import com.example.mynewsapp.domain.interfaces.RetrofitRepository
import javax.inject.Inject

class RetrofitRepositoryImpl @Inject constructor(var requestService: RequestService) :
    RetrofitRepository {


    override suspend fun getLatestNews(
        keyword: String,
        sortBy: String,
        pageSize: Int?,
        page: Int?,
    ): Result<List<ArticleModel>> {
        return try {
            val response = requestService.getLatestNews(
                keyword, sortBy, pageSize, page, com.example.mynewsapp.Utils.Constants.API_KEY
            )
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it.articles.map { it.toDomain() })
                } ?: Result.failure(Exception("Response is empty"))
            } else {
                Result.failure(Exception("Error:${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }

    }
}