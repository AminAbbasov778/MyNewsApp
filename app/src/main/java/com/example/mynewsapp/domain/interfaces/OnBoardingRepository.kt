package com.example.mynewsapp.domain.interfaces

import com.example.mynewsapp.data.model.boarding.Boarding
import com.example.mynewsapp.presentation.uimodels.boarding.BoardingUiModel

interface OnBoardingRepository {
    fun getOnBoardingData(): List<Boarding>
}