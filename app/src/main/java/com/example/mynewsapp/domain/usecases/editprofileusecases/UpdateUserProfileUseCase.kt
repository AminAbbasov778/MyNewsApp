package com.example.mynewsapp.domain.usecases.editprofileusecases

import com.example.mynewsapp.data.model.userprofile.UserProfileModel
import com.example.mynewsapp.domain.interfaces.UserRepository
import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor(val userRepository: UserRepository) {

    suspend operator fun invoke(userProfileModel: UserProfileModel): Result<Unit> =
        userRepository.uploadProfileData(userProfileModel)

}