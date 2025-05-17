package com.example.mynewsapp.domain.usecases.commonusecases

import com.example.mynewsapp.domain.domainmodels.ArticleModel
import javax.inject.Inject

class ProcessSearchedNewsUseCase @Inject constructor() {
    operator fun invoke(newsList: List<ArticleModel>, query: String): List<ArticleModel> {
        return newsList.filter { article ->
            article.title?.contains(query, ignoreCase = true) == true ||
                    article.description?.contains(query, ignoreCase = true) == true ||
                    article.author?.contains(query, ignoreCase = true) == true ||
                    article.source?.name?.contains(query, ignoreCase = true) == true
        }
    }
}