package com.example.mynewsapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mynewsapp.R
import com.example.mynewsapp.domain.usecases.commonusecases.GettingUserEmailUseCase
import com.example.mynewsapp.domain.usecases.mainusecases.IsFirstLaunchUseCase
import com.example.mynewsapp.presentation.uistates.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(val gettingUserEmailUseCase: GettingUserEmailUseCase,val isFirstLaunchUseCase: IsFirstLaunchUseCase) :
    ViewModel() {

    private var _isUserLoggedIn = MutableLiveData<ResultState<Boolean>>()
    val isUserLoggedIn: LiveData<ResultState<Boolean>> get() = _isUserLoggedIn

    private var _isFirstLaunch = MutableLiveData<Boolean>()
    val isFirstLaunch : LiveData<Boolean> get() = _isFirstLaunch

init {
    isFirstLaunch()
}

    fun checkUserInfo() {
        val userEmailResult = gettingUserEmailUseCase()

        if (userEmailResult.isSuccess) {
            val userEmail = userEmailResult.getOrNull()
            _isUserLoggedIn.value =   if (userEmail!!.isNotEmpty())  ResultState.Success(true) else  ResultState.Success(false)
        }
        else{
            _isUserLoggedIn.value = ResultState.Error(R.string.wrong_something)
        }
    }

    fun isFirstLaunch(){
      _isFirstLaunch.value =  isFirstLaunchUseCase()
    }


}