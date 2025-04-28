package com.example.mynewsapp.domain.usecases.mainusecases

import com.example.mynewsapp.domain.interfaces.SharedPreferenceRepository
import javax.inject.Inject

class IsFirstLaunchUseCase @Inject constructor(val sharedPreferenceRepository: SharedPreferenceRepository) {
    operator fun invoke(): Boolean = sharedPreferenceRepository.isFirstLaunch()

}