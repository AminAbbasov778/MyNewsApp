package com.example.mynewsapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mynewsapp.domain.usecases.onboardingusecases.GetOnBoardingDataUseCase
import com.example.mynewsapp.domain.usecases.onboardingusecases.MarkFirstLaunchDoneUseCase
import com.example.mynewsapp.presentation.mappers.toUi
import com.example.mynewsapp.presentation.uimodels.boarding.BoardingUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(val markFirstLaunchDoneUseCase: MarkFirstLaunchDoneUseCase,val getOnBoardingDataUseCase: GetOnBoardingDataUseCase) : ViewModel() {


   private  val _boardingUiModelList = MutableLiveData<List<BoardingUiModel>>()
    val boardingUiModelList: LiveData<List<BoardingUiModel>> get() = _boardingUiModelList


    init {
        _boardingUiModelList.value = getOnBoardingDataUseCase().map { it.toUi() }
    }


    fun markFirstLaunchDone(isFirstLaunchOver: Boolean) = markFirstLaunchDoneUseCase(isFirstLaunchOver)







}