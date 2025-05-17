package com.example.mynewsapp.domain.usecases.profileUseCase

import com.example.mynewsapp.domain.domainmodels.UserNewsModel
import javax.inject.Inject

class ReverseUserNewsListUseCase @Inject constructor() {
    operator fun invoke(list : List<UserNewsModel>): List<UserNewsModel> = list.reversed()
}