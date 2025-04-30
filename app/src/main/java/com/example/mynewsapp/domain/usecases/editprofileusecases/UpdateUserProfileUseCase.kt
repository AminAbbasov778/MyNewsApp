package com.example.mynewsapp.domain.usecases.editprofileusecases

import com.example.mynewsapp.domain.interfaces.UserRepository
import com.example.mynewsapp.domain.mappers.toDomain
import com.example.mynewsapp.presentation.uimodels.profile.NewProfileUiModel
import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor(val userRepository: UserRepository) {

    suspend operator fun invoke(userProfile: NewProfileUiModel): Result<Unit> =
        userRepository.uploadProfileData(userProfile.toDomain())

}