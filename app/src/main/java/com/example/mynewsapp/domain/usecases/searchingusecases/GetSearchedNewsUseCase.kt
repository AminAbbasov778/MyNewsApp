package com.example.mynewsapp.domain.usecases.search

import com.example.mynewsapp.data.model.latestnews.Article
import com.example.mynewsapp.domain.domainmodels.ArticleModel
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
    ): Result<List<ArticleModel>> {
        val trimmedQuery = query.trim()
        if (trimmedQuery.isEmpty()) {
            return Result.success(emptyList())
        }
        val result = getProcessedNewsUseCase(keyWord, sortBy, pageSize, page)
        return result.mapCatching { articles ->
            processSearchedNewsUseCase(articles, trimmedQuery)
        }
    }
}
