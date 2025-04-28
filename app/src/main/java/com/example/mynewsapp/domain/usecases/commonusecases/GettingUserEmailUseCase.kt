package com.example.mynewsapp.domain.usecases.commonusecases

import com.example.mynewsapp.domain.interfaces.SharedPreferenceRepository
import javax.inject.Inject

class GettingUserEmailUseCase @Inject constructor(
    private val sharedPreferenceRepository: SharedPreferenceRepository,
) {
    operator fun invoke(): Result<String> {
        return sharedPreferenceRepository.getUserEmail()

    }
}