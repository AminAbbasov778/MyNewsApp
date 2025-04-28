package com.example.mynewsapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mynewsapp.R
import com.example.mynewsapp.domain.usecases.onboardingusecases.MarkFirstLaunchDoneUseCase
import com.example.mynewsapp.presentation.uimodels.boarding.BoardingModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(val markFirstLaunchDoneUseCase: MarkFirstLaunchDoneUseCase) : ViewModel() {


   private  val _boardingModelList = MutableLiveData<List<BoardingModel>>()
    val boardingModelList: LiveData<List<BoardingModel>> get() = _boardingModelList


    init {
        _boardingModelList.value = listOf(
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


    fun markFirstLaunchDone(isFirstLaunchOver: Boolean) = markFirstLaunchDoneUseCase(isFirstLaunchOver)







}