package com.example.mynewsapp.domain.usecases.search

import com.example.mynewsapp.data.model.latestnews.Article
import javax.inject.Inject

class ProcessSearchedNewsUseCase @Inject constructor() {
    operator fun invoke(newsList: List<Article>, query: String): List<Article> {
        return newsList.filter { article ->
            article.title?.contains(query, ignoreCase = true) == true ||
                    article.description?.contains(query, ignoreCase = true) == true ||
                    article.author?.contains(query, ignoreCase = true) == true ||
                    article.source?.name?.contains(query, ignoreCase = true) == true
        }
    }
}
