package com.example.mynewsapp.domain.usecases.signupusecases

import com.example.mynewsapp.domain.domainstates.DomainValidationState
import javax.inject.Inject

class PasswordSpecialCharacterValidationUseCase  @Inject constructor(){
    operator fun invoke(password: String): DomainValidationState {
        val regex = ".*[@#\$%^&+=].*"
        if (password.matches(regex.toRegex())) {

            return DomainValidationState.Success
        } else {
            return DomainValidationState.Error.NoSpecialChar
        }
    }
}