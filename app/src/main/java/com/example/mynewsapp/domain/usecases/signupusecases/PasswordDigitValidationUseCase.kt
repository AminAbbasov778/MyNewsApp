package com.example.mynewsapp.domain.usecases.signupusecases

import com.example.mynewsapp.domain.domainstates.DomainValidationState
import javax.inject.Inject

class PasswordDigitValidationUseCase  @Inject constructor() {
    operator fun invoke(password: String): DomainValidationState {
        var regex = ".*\\d.*"
        if (password.matches(regex.toRegex())) {
            return DomainValidationState.Success
        } else {
            return DomainValidationState.Error.NoDigit
        }
    }
}