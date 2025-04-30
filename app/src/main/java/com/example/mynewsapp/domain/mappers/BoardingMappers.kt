package com.example.mynewsapp.domain.mappers

import com.example.mynewsapp.data.model.boarding.Boarding
import com.example.mynewsapp.domain.domainmodels.BoardingModel

fun Boarding.toDomain(): BoardingModel{
    return BoardingModel(img,title,description)
}