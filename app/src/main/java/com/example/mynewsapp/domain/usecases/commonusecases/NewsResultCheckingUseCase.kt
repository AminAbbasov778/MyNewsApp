package com.example.mynewsapp.domain.usecases.commonusecases

import com.example.mynewsapp.data.model.latestnews.LatestNewsResponse
import com.example.mynewsapp.domain.interfaces.RetrofitRepository
import javax.inject.Inject

class NewsResultCheckingUseCase @Inject constructor(
    private val retrofitRepository: RetrofitRepository,
) {
    suspend operator fun invoke(
        keyWord: String,
        sortBy: String,
        pageSize: Int?,
        page: Int? = null,
    ): Result<LatestNewsResponse> {

        return retrofitRepository.getLatestNews(keyWord, sortBy, pageSize, page)

    }
}