package com.example.mynewsapp.data.repositories

import com.example.mynewsapp.R
import com.example.mynewsapp.data.mappers.toDomain
import com.example.mynewsapp.data.model.boarding.Boarding
import com.example.mynewsapp.domain.domainmodels.BoardingModel
import com.example.mynewsapp.domain.interfaces.OnBoardingRepository
import javax.inject.Inject

class OnBoardingRepositoryImpl @Inject constructor() : OnBoardingRepository {

    override fun getOnBoardingData(): List<BoardingModel> {
        val list = listOf(
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

        return list.map { it.toDomain() }
    }
}