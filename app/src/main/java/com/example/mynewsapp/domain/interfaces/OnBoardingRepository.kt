package com.example.mynewsapp.domain.interfaces

import com.example.mynewsapp.domain.domainmodels.BoardingModel

interface OnBoardingRepository {
    fun getOnBoardingData(): List<BoardingModel>
}