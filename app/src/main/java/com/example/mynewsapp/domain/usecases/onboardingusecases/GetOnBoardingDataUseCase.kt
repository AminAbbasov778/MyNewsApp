package com.example.mynewsapp.domain.usecases.onboardingusecases

import com.example.mynewsapp.domain.domainmodels.BoardingModel
import com.example.mynewsapp.domain.interfaces.OnBoardingRepository
import javax.inject.Inject

class GetOnBoardingDataUseCase @Inject constructor(val onBoardingRepository: OnBoardingRepository) {
    operator fun invoke(): List<BoardingModel> = onBoardingRepository.getOnBoardingData()
}