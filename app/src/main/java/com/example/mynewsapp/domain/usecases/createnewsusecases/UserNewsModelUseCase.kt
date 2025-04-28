package com.example.mynewsapp.domain.usecases.createnewsusecases

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.mynewsapp.data.model.usernews.UserNewsModel
import com.example.mynewsapp.domain.interfaces.UserRepository
import com.example.mynewsapp.domain.usecases.editprofileusecases.ConvertUriToBase64UseCase
import javax.inject.Inject

class UserNewsModelUseCase @Inject constructor(val convertUriToBase64UseCase: ConvertUriToBase64UseCase,
    val timeStampUseCase: GetTimeStampUseCase,
    val userRepository: UserRepository

) {
    @RequiresApi(Build.VERSION_CODES.O)
    operator fun invoke(newsTitle : String, newsArticle : String, newsImageUri : Uri,fullName : String,profileImageUri : String): UserNewsModel{
        val newsImageBase64 = convertUriToBase64UseCase(newsImageUri)
        val publishedAt = timeStampUseCase()
        return UserNewsModel(newsArticle,newsTitle,newsImageBase64 ?: "",profileImageUri,fullName,publishedAt)
    }
}