package com.example.mynewsapp.domain.usecases.editprofileusecases

import com.example.mynewsapp.domain.domainmodels.ProfileModel
import com.example.mynewsapp.domain.interfaces.UserRepository
import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor(val userRepository: UserRepository) {

    suspend operator fun invoke(profileModel: ProfileModel): Result<Unit> =
        userRepository.uploadProfileData(profileModel)

}