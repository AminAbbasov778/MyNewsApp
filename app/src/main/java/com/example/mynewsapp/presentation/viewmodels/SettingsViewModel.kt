package com.example.mynewsapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynewsapp.R
import com.example.mynewsapp.domain.usecases.profileUseCase.LogoutUseCase
import com.example.mynewsapp.domain.usecases.profileUseCase.RemoveUserLoginInfoUseCase
import com.example.mynewsapp.presentation.uistates.ResultState
import com.example.mynewsapp.presentation.uimodels.settings.SettingsModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor( var logOutUseCase: LogoutUseCase,
                                             var removeUserLoginInfoUseCase: RemoveUserLoginInfoUseCase,) : ViewModel() {

    private var _settingsType = MutableLiveData<ArrayList<SettingsModel>>()
    val settingsType: LiveData<ArrayList<SettingsModel>> get() = _settingsType

    private var _isLoggedOut = MutableLiveData<ResultState<Int>>()
    val isLoggedOut: LiveData<ResultState<Int>> get() = _isLoggedOut

    private val _navigation = MutableLiveData<SettingsModel?>()
    val navigation: LiveData<SettingsModel?> get() = _navigation

    init {
        loadTypes()
    }

    fun loadTypes() {
        val settingsTypes = arrayListOf(
            SettingsModel(
                R.drawable.notification_icon,
                R.string.notification,

            ),
            SettingsModel(R.drawable.lock_icon, R.string.security),
            SettingsModel(R.drawable.help_icon, R.string.help),
            SettingsModel(R.drawable.language_icon, R.string.language),
            SettingsModel(R.drawable.display_icon, R.string.display),
            SettingsModel(R.drawable.logout_icon, R.string.logout),
        )
        _settingsType.value = settingsTypes
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

    fun onSettingItemClick(setting : SettingsModel){
        _navigation.value = setting
    }
    fun clearData(){
        _navigation.value = null
    }
}