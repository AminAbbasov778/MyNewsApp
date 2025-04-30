package com.example.mynewsapp.domain.interfaces

import com.example.mynewsapp.presentation.uimodels.boarding.BoardingModel

interface OnBoardingRepository {
    fun getOnBoardingData(): List<BoardingModel>
}