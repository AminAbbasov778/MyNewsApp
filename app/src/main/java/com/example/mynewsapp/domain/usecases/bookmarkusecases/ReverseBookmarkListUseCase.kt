package com.example.mynewsapp.domain.usecases.bookmark

import com.example.mynewsapp.domain.domainmodels.ArticleModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ReverseBookmarkListUseCase @Inject constructor() {
    operator fun invoke(bookmarks: Flow<List<ArticleModel>>): Flow<List<ArticleModel>> {
        return bookmarks.map { it.reversed() }
    }
}