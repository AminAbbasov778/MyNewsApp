package com.example.mynewsapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mynewsapp.R
import com.example.mynewsapp.domain.usecases.displayusecases.GetThemeUseCase
import com.example.mynewsapp.domain.usecases.displayusecases.SaveThemeUseCase
import com.example.mynewsapp.presentation.uistates.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DisplayViewModel @Inject constructor(
    val getThemeUseCase: GetThemeUseCase,
    val saveThemeUseCase: SaveThemeUseCase,
) : ViewModel() {
    private var _isDarkMode = MutableLiveData<ResultState<Boolean>?>()
    val isDarkMode: LiveData<ResultState<Boolean>?> = _isDarkMode

    init {
        getTheme()
    }

    fun getTheme() {

        val result = getThemeUseCase()

        if (result.isSuccess) {
            _isDarkMode.value = ResultState.Success(result.getOrNull() ?: false)
        } else {
            _isDarkMode.value = ResultState.Error(R.string.failure_color_mode)
        }

    }


    fun saveTheme(isDarkMode: Boolean) {
        val result = saveThemeUseCase(isDarkMode)
        if (result.isSuccess) {
            _isDarkMode.value = ResultState.Success(isDarkMode)
        } else{
            _isDarkMode.value = ResultState.Error(R.string.failure_color_mode)
        }
    }


}