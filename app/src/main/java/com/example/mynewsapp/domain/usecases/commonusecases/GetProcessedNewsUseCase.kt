package com.example.mynewsapp.domain.usecases.commonusecases

import com.example.mynewsapp.domain.domainmodels.ArticleModel
import javax.inject.Inject

class GetProcessedNewsUseCase @Inject constructor(
    var getNewsUseCase: GetNewsUseCase,
    var addTimeDifferenceToNewsUseCase: AddTimeDifferenceToNewsUseCase,
) {
    suspend operator fun invoke(
        keyWord: String,
        sortBy: String,
        pageSize: Int? = null,
        page: Int? = null,
    ): Result<List<ArticleModel>> {
        val news = getNewsUseCase(
            keyWord, sortBy, pageSize, page
        )
      return news.map {
           it.map { addTimeDifferenceToNewsUseCase(it) }
      }




    }
}
