package com.example.mynewsapp.domain.usecases.commonusecases

import com.example.mynewsapp.domain.domainmodels.ArticleModel
import javax.inject.Inject

class AddTimeDifferenceToNewsUseCase @Inject constructor(
    private val timeDifferenceUseCase: TimeDifferenceUseCase,
    private val changeIsoToMillisFromApiUseCase: ChangeIsoToMillisFromApiUseCase,
) {
    operator fun invoke(news: ArticleModel): ArticleModel {
        return news.copy(
            timeDifference = timeDifferenceUseCase(
                changeIsoToMillisFromApiUseCase(news.publishedAt ?: "No published at")
            )
        )
    }
}