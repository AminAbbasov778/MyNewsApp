package com.example.mynewsapp.domain.usecases.search

import com.example.mynewsapp.data.model.latestnews.Article
import com.example.mynewsapp.domain.usecases.commonusecases.GetProcessedNewsUseCase
import javax.inject.Inject

class GetSearchedNewsUseCase @Inject constructor(
    private val getProcessedNewsUseCase: GetProcessedNewsUseCase,
    private val processSearchedNewsUseCase: ProcessSearchedNewsUseCase,
) {
    suspend operator fun invoke(
        keyWord: String = "all",
        sortBy: String = "publishedAt",
        pageSize: Int? = null,
        query: String,
        page: Int? = null,
    ): Result<List<Article>> {
        val result = getProcessedNewsUseCase(keyWord, sortBy, pageSize, page)
        return if (result.isSuccess) {
            val newsList = result.getOrNull() ?: emptyList()
            val searchedNews = processSearchedNewsUseCase(newsList, query)
            Result.success(searchedNews)
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }
}