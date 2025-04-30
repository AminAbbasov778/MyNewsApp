package com.example.mynewsapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynewsapp.R
import com.example.mynewsapp.domain.usecases.settingsusecases.GetSettingsTypesUseCases
import com.example.mynewsapp.domain.usecases.settingsusecases.LogoutUseCase
import com.example.mynewsapp.domain.usecases.settingsusecases.RemoveUserLoginInfoUseCase
import com.example.mynewsapp.presentation.mappers.toUi
import com.example.mynewsapp.presentation.uistates.ResultState
import com.example.mynewsapp.presentation.uimodels.settings.SettingsUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor( var logOutUseCase: LogoutUseCase,
                                             var removeUserLoginInfoUseCase: RemoveUserLoginInfoUseCase,
    val getSettingsTypesUseCases: GetSettingsTypesUseCases
    ) : ViewModel() {

    private var _settingsType = MutableLiveData<List<SettingsUiModel>>()
    val settingsType: LiveData<List<SettingsUiModel>> get() = _settingsType

    private var _isLoggedOut = MutableLiveData<ResultState<Int>>()
    val isLoggedOut: LiveData<ResultState<Int>> get() = _isLoggedOut

    private val _navigation = MutableLiveData<SettingsUiModel?>()
    val navigation: LiveData<SettingsUiModel?> get() = _navigation

    init {
        loadTypes()
    }

    fun loadTypes() {
        _settingsType.value = getSettingsTypesUseCases().map { it.toUi() }
    }

    fun logOut() {
           viewModelScope.launch {
               val isLoggedOutResult = async {
                   logOutUseCase()
               }.await()
               val isRemovedResult = async {
                   removeUserLoginInfoUseCase()
               }.await()

               if (isLoggedOutResult.isSuccess && isRemovedResult.isSuccess) {
                   _isLoggedOut.value = ResultState.Success(R.string.successful_logout)
               } else {
                   _isLoggedOut.value = ResultState.Error(R.string.failure_logout)
               }


           }

    }

    fun onSettingItemClick(setting : SettingsUiModel){
        _navigation.value = setting
    }
    fun clearData(){
        _navigation.value = null
    }
}