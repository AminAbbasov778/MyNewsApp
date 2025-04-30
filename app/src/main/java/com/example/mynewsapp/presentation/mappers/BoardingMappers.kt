package com.example.mynewsapp.presentation.mappers

import com.example.mynewsapp.domain.domainmodels.BoardingModel
import com.example.mynewsapp.presentation.uimodels.boarding.BoardingUiModel

fun BoardingModel.toUi(): BoardingUiModel{
    return BoardingUiModel(img,title,description)
}