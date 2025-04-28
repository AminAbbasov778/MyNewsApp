package com.example.mynewsapp.domain.usecases.commonusecases

import com.example.mynewsapp.data.model.latestnews.Article
import com.example.mynewsapp.data.model.latestnews.Source
import javax.inject.Inject

class AddTimeDifferenceToNewsUseCase @Inject constructor(
    private val timeDifferenceUseCase: TimeDifferenceUseCase,
    private val changeIsoToMillisFromApiUseCase: ChangeIsoToMillisFromApiUseCase,
) {
    operator fun invoke(news: Article): Article {
        return news.copy(
            urlToImage = news.urlToImage ?: "No Image Url",
            timeDifference = timeDifferenceUseCase(
                changeIsoToMillisFromApiUseCase(news.publishedAt ?: "No published at")
            ),
            title = news.title ?: "No title",
            description = news.description ?: "No description",
            author = news.author ?: "No author",
            content = news.content ?: "No content",
            source = Source(news.source?.id ?: "No id", news.source?.name ?: "No name"),
            url = news.url ?: "No url",
            publishedAt = news.publishedAt ?: "No published at"
        )
    }
}