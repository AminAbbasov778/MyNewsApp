package com.example.mynewsapp.domain.usecases.editprofileusecases

import android.net.Uri
import com.example.mynewsapp.data.model.userprofile.UserProfileModel
import javax.inject.Inject

class UserProfileModelUseCase @Inject constructor(val convertUriToBase64UseCase: ConvertUriToBase64UseCase) {
    operator fun invoke(
        fullName: String,
        bio: String,
        email: String,
        imageUri: Uri?,
        userName: String,
        phoneNumber: String,
        website: String,

        ): UserProfileModel {
        val imageBase64 = convertUriToBase64UseCase(imageUri)
        return UserProfileModel(fullName, bio, email, imageBase64 ?: "", userName, phoneNumber, website)
    }
}