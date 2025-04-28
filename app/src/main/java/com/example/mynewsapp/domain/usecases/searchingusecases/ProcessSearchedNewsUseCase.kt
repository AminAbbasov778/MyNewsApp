package com.example.mynewsapp.domain.usecases.search

import com.example.mynewsapp.data.model.latestnews.Article

class ProcessSearchedNewsUseCase {
    operator fun invoke(newsList: List<Article>, query: String): List<Article> {
        val trimmedQuery = query.trim()
        if (newsList.isNotEmpty()) {
            if (trimmedQuery.isEmpty()) {
                return emptyList()
            } else {
                return newsList.filter { article ->
                    article.title?.contains(trimmedQuery, ignoreCase = true) == true ||
                            article.description?.contains(
                                trimmedQuery,
                                ignoreCase = true
                            ) == true ||
                            article.author?.contains(trimmedQuery, ignoreCase = true) == true ||
                            article.source?.name?.contains(trimmedQuery, ignoreCase = true) == true
                }
            }
        } else {
            return emptyList()
        }

    }
}