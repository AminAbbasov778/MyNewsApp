package com.example.mynewsapp.domain.usecases.searchingusecases

import com.example.mynewsapp.domain.domainmodels.AuthorModel
import javax.inject.Inject

class SearchAuthorsUseCase @Inject constructor() {
    operator fun invoke(list: List<AuthorModel>, query: String): List<AuthorModel> {
        return list.filter { it.sourceName.contains(query, true) }
    }
}