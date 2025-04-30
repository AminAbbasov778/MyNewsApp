package com.example.mynewsapp.data.repositories

import com.example.mynewsapp.R
import com.example.mynewsapp.domain.interfaces.OnBoardingRepository
import com.example.mynewsapp.presentation.uimodels.boarding.BoardingModel
import javax.inject.Inject

class OnBoardingRepositoryImpl @Inject constructor():OnBoardingRepository {

    override fun getOnBoardingData(): List<BoardingModel> =  listOf(
        BoardingModel(
            R.drawable.newsboardingimage1,
            R.string.boarding_1_title,
            R.string.boarding_1_description
        ),
        BoardingModel(
            R.drawable.newsboardingimage2,
            R.string.boarding_2_title,
            R.string.boarding_2_description
        ),
        BoardingModel(
            R.drawable.newsboardingimage3,
            R.string.boarding_3_title,
            R.string.boarding_3_description
        )
    )
}