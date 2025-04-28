package com.example.mynewsapp.domain.usecases.onboardingusecases

import com.example.mynewsapp.domain.interfaces.SharedPreferenceRepository
import javax.inject.Inject

class MarkFirstLaunchDoneUseCase @Inject constructor(val sharedPreferenceRepository: SharedPreferenceRepository) {

    operator fun invoke(isFirstLaunchOver: Boolean) =
        sharedPreferenceRepository.markFirstLaunchDone(isFirstLaunchOver)


}