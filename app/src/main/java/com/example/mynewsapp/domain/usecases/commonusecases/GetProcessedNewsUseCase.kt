package com.example.mynewsapp.domain.usecases.commonusecases

import com.example.mynewsapp.data.model.latestnews.Article
import javax.inject.Inject

class GetProcessedNewsUseCase @Inject constructor(
    var newsResultCheckingUseCase: NewsResultCheckingUseCase,
    var addTimeDifferenceToNewsUseCase: AddTimeDifferenceToNewsUseCase,
) {
    suspend operator fun invoke(
        keyWord: String,
        sortBy: String,
        pageSize: Int? = null,
        page: Int? = null,
    ): Result<ArrayList<Article>> {
        val latestNewsResponse = newsResultCheckingUseCase(
            keyWord, sortBy, pageSize, page
        )
        val latestNewsList = arrayListOf<Article>()
        return if (latestNewsResponse.isSuccess) {
            val latestNews = latestNewsResponse.getOrNull()
            latestNews?.let {
                for (news in it.articles) {
                    val updatedNews = addTimeDifferenceToNewsUseCase(news)
                    latestNewsList.add(updatedNews)

                }

            }
            Result.success(latestNewsList)

        } else {
            Result.failure(latestNewsResponse.exceptionOrNull() ?: Exception("Unknown error"))
        }


    }
}