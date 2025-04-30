package com.example.mynewsapp.domain.usecases.signupusecases

import com.example.mynewsapp.domain.domainstates.DomainValidationState
import javax.inject.Inject

class PasswordLowerCaseValidationUseCase  @Inject constructor() {
    operator fun invoke(password: String): DomainValidationState {
        var regex = ".*[a-z].*"
        if (password.matches(regex.toRegex())) {
            return DomainValidationState.Success
        } else {
            return DomainValidationState.Error.NoLowerCase
        }
    }
}