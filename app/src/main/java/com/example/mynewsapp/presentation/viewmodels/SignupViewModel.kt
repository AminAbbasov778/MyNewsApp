package com.example.mynewsapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynewsapp.R
import com.example.mynewsapp.domain.domainstates.DomainValidationState
import com.example.mynewsapp.domain.usecases.signupusecases.SignupUserUseCase
import com.example.mynewsapp.domain.usecases.signupusecases.SignupValidationUseCase
import com.example.mynewsapp.presentation.uistates.UiState
import com.example.mynewsapp.presentation.uistates.ValidationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    var signupValidationUseCase: SignupValidationUseCase,
    var signupUserUseCase: SignupUserUseCase

) : ViewModel() {

    private var _validationState = MutableLiveData<ValidationState>()
    val validationState: LiveData<ValidationState> get() = _validationState

    private var _signupState = MutableLiveData<UiState<Int>>()
    val signupState: LiveData<UiState<Int>> get() = _signupState

    fun prePasswordValidation(email: String, password: String, confirmPassword: String) {
        val signupValidationResult = signupValidationUseCase(
            email,
            password,
            confirmPassword,

        )

        _validationState.value = when(signupValidationResult) {
            DomainValidationState.Success -> ValidationState.Success(R.string.successful_validation)
            DomainValidationState.Error.EmptyField -> ValidationState.Error.EmptyField(R.string.empty_fields_message)
            DomainValidationState.Error.NoDigit -> ValidationState.Error.UnsafetyPassword(R.string.no_digit)
            DomainValidationState.Error.NoUpperCase -> ValidationState.Error.UnsafetyPassword(R.string.no_upper_case)
            DomainValidationState.Error.NoLowerCase -> ValidationState.Error.UnsafetyPassword(R.string.no_lower_case)
            DomainValidationState.Error.NoSpecialChar -> ValidationState.Error.UnsafetyPassword(R.string.no_special_character)
            DomainValidationState.Error.ShortPassword -> ValidationState.Error.UnsafetyPassword(R.string.short_password_message)
            DomainValidationState.Error.InvalidEmail -> ValidationState.Error.InvalidEmail(R.string.invalid_email_message)
            DomainValidationState.Error.ConfirmPasswordDismatch -> ValidationState.Error.ConfirmPasswordDismatch(R.string.confirm_password)

        }
    }

    fun getSignupResult(email: String, password: String) {
        _signupState.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val signupResult = signupUserUseCase(email, password)
            withContext(Dispatchers.Main){
                _signupState.value = if (signupResult.isSuccess) {
                    UiState.Success(R.string.successful_signup)
                } else {
                    UiState.Success(R.string.failure_signup)
                }
            }

        }
    }
}