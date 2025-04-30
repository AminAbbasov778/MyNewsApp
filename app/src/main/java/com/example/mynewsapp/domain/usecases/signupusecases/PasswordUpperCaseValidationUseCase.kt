package com.example.mynewsapp.domain.usecases.signupusecases

import com.example.mynewsapp.domain.domainstates.DomainValidationState
import javax.inject.Inject

class PasswordUpperCaseValidationUseCase  @Inject constructor() {
    operator fun invoke(password: String): DomainValidationState {
        val regex = ".*[A-Z].*"
        if (password.matches(regex.toRegex())) {
            return DomainValidationState.Success
        } else {
            return DomainValidationState.Error.NoUpperCase
        }
    }
}