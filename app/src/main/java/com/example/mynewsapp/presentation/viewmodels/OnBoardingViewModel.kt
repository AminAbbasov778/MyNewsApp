package com.example.mynewsapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mynewsapp.R
import com.example.mynewsapp.domain.usecases.onboardingusecases.GetOnBoardingDataUseCase
import com.example.mynewsapp.domain.usecases.onboardingusecases.MarkFirstLaunchDoneUseCase
import com.example.mynewsapp.presentation.uimodels.boarding.BoardingModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(val markFirstLaunchDoneUseCase: MarkFirstLaunchDoneUseCase,val getOnBoardingDataUseCase: GetOnBoardingDataUseCase) : ViewModel() {


   private  val _boardingModelList = MutableLiveData<List<BoardingModel>>()
    val boardingModelList: LiveData<List<BoardingModel>> get() = _boardingModelList


    init {
        _boardingModelList.value = getOnBoardingDataUseCase()
    }


    fun markFirstLaunchDone(isFirstLaunchOver: Boolean) = markFirstLaunchDoneUseCase(isFirstLaunchOver)







}