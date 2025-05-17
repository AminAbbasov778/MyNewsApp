package com.example.mynewsapp.domain.usecases.commonusecases

import com.example.mynewsapp.domain.domainmodels.ArticleModel
import com.example.mynewsapp.domain.interfaces.RetrofitRepository
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
      return retrofitRepository.getLatestNews(keyWord, sortBy, pageSize, page)
    }
}