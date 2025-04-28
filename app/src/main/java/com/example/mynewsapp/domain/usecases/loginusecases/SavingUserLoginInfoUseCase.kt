package com.example.mynewsapp.domain.usecases.loginusecases

import com.example.mynewsapp.domain.interfaces.SharedPreferenceRepository
import javax.inject.Inject

class SavingUserLoginInfoUseCase @Inject constructor(
    private val sharedPreferenceRepository: SharedPreferenceRepository,
) {
    operator fun invoke(email: String, password: String, state: Boolean) {
        if (state) {
            sharedPreferenceRepository.saveUserLoginInfo(email, password)
        }
    }
}