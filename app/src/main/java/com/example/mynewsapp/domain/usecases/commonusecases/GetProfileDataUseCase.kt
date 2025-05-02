package com.example.mynewsapp.domain.usecases.commonusecases

import com.example.mynewsapp.data.model.userprofile.Profile
import com.example.mynewsapp.domain.domainmodels.ProfileModel
import com.example.mynewsapp.domain.interfaces.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProfileDataUseCase @Inject constructor(val userRepository: UserRepository) {

    suspend operator fun invoke(): Flow<Result<ProfileModel>> = userRepository.getProfileData()

}