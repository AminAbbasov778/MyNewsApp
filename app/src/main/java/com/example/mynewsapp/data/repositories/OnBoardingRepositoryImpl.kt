package com.example.mynewsapp.data.repositories

import com.example.mynewsapp.R
import com.example.mynewsapp.data.model.boarding.Boarding
import com.example.mynewsapp.domain.interfaces.OnBoardingRepository
import com.example.mynewsapp.presentation.uimodels.boarding.BoardingUiModel
import javax.inject.Inject

class OnBoardingRepositoryImpl @Inject constructor() : OnBoardingRepository {

    override fun getOnBoardingData(): List<Boarding> = listOf(
        Boarding(
            R.drawable.newsboardingimage1,
            R.string.boarding_1_title,
            R.string.boarding_1_description
        ),
        Boarding(
            R.drawable.newsboardingimage2,
            R.string.boarding_2_title,
            R.string.boarding_2_description
        ),
        Boarding(
            R.drawable.newsboardingimage3,
            R.string.boarding_3_title,
            R.string.boarding_3_description
        )
    )
}