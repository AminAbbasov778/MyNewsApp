package com.example.mynewsapp.domain.usecases.createnewsusecases

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.mynewsapp.data.model.usernews.UserNews
import com.example.mynewsapp.domain.usecases.editprofileusecases.ConvertUriToBase64UseCase
import javax.inject.Inject

class UserNewsModelUseCase @Inject constructor(val convertUriToBase64UseCase: ConvertUriToBase64UseCase,
    val timeStampUseCase: GetTimeStampUseCase,

) {
    @RequiresApi(Build.VERSION_CODES.O)
    operator fun invoke(newsTitle : String, newsArticle : String, newsImageUri : Uri,fullName : String,profileImageUri : String): UserNews{
        val newsImageBase64 = convertUriToBase64UseCase(newsImageUri)
        val publishedAt = timeStampUseCase()
        return UserNews(newsArticle,newsTitle,newsImageBase64 ?: "",profileImageUri,fullName,publishedAt)
    }
}