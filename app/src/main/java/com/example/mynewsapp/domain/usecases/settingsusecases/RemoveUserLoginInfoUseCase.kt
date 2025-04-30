package com.example.mynewsapp.domain.usecases.settingsusecases

import com.example.mynewsapp.domain.interfaces.SharedPreferenceRepository
import javax.inject.Inject

class RemoveUserLoginInfoUseCase @Inject constructor(var sharedPreferenceRepository: SharedPreferenceRepository) {
    operator fun invoke(): Result<Boolean> = sharedPreferenceRepository.removeUserLoginInfo()


}