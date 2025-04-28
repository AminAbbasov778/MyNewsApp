package com.example.mynewsapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynewsapp.R
import com.example.mynewsapp.domain.domainstates.DomainValidationState
import com.example.mynewsapp.domain.interfaces.RegisterRepository
import com.example.mynewsapp.domain.usecases.loginusecases.LoginResultUseCase
import com.example.mynewsapp.domain.usecases.loginusecases.LoginValidationUseCase
import com.example.mynewsapp.domain.usecases.loginusecases.SavingUserLoginInfoUseCase
import com.example.mynewsapp.domain.usecases.loginusecases.SignInWithFacebookUseCase
import com.example.mynewsapp.domain.usecases.loginusecases.SignInWithGoogleUseCase
import com.example.mynewsapp.presentation.uistates.UiState
import com.example.mynewsapp.presentation.uistates.ValidationState
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val loginValidationUseCase: LoginValidationUseCase,
    val getLoginResultUseCase: LoginResultUseCase,
    val savingUserLoginInfoUseCase: SavingUserLoginInfoUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val signInWithFacebookUseCase: SignInWithFacebookUseCase,
    val registerRepository: RegisterRepository,
) : ViewModel() {

    private val _validationState = MutableLiveData<ValidationState?>()
    val validationState: LiveData<ValidationState?> get() = _validationState

    private val _loginState = MutableLiveData<UiState<Int>?>()
    val loginState: LiveData<UiState<Int>?> get() = _loginState




    fun getCheckingResultBeforeLogin(email: String, password: String) {
        val loginValidationResult =
            loginValidationUseCase(
                email,
                password,

                )
        _validationState.value = when (loginValidationResult) {
            is DomainValidationState.Error.EmptyField -> ValidationState.Error.EmptyField(R.string.empty_fields_message)
            is DomainValidationState.Error.InvalidEmail -> ValidationState.Error.InvalidEmail(R.string.invalid_email_message)
            is DomainValidationState.Error.ShortPassword -> ValidationState.Error.ShortPassword(R.string.short_password_message)
            else -> ValidationState.Success(R.string.successful_validation)
        }
    }

    fun getLoginResult(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = UiState.Loading
            val loginResult = getLoginResultUseCase(email, password)
            _loginState.value = if (loginResult.isSuccess) {
                UiState.Success(R.string.successful_login)
            } else {
                UiState.Error(R.string.failure_login)
            }
        }
    }

    fun saveUserLoginInfo(email: String, password: String, state: Boolean) {
        savingUserLoginInfoUseCase(email, password, state)
    }

    fun clearLiveData() {
        _validationState.value = null
        _loginState.value = null
    }


    fun signInWithGoogle(idToken: String) {
        _loginState.value = UiState.Loading
        viewModelScope.launch {
            val result = signInWithGoogleUseCase(idToken)
            if (result.isSuccess) {
                _loginState.value = UiState.Success(R.string.successful_login)
            }

            if (result.isFailure) {
                _loginState.value = UiState.Error(R.string.failure_login)
            }
        }
    }


    fun signInWithFacebook(token: String) {
        _loginState.value = UiState.Loading
        viewModelScope.launch {
            val result = signInWithFacebookUseCase(token)
            if (result.isSuccess) {
                _loginState.value = UiState.Success(R.string.successful_login)
            }

            if (result.isFailure) {
                _loginState.value = UiState.Error(R.string.failure_login)
            }
        }
    }





}






